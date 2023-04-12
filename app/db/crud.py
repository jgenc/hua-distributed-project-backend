from sqlalchemy import update, Update
from sqlalchemy.orm import Session

from . import models
from .. import schemas

# TODO: Should I make the methods static?
# And even if I do, is there a point of creating the classes? Maybe a module
# would be better?


class Users:
    def get_user(self, db: Session, user_id: int):
        return db.query(models.User).filter(models.User.id == user_id).first()

    def get_users(self, db: Session, skip: int = 0, limit: int = 10):
        return db.query(models.User).offset(skip).limit(limit).all()

    def create_user(self, db: Session, user: schemas.UserCreate):
        not_really_hashed = user.password + "_hashed"
        db_user = models.User(
            password=not_really_hashed, username=user.username, tin=user.tin
        )
        db.add(db_user)
        db.commit()
        db.refresh(db_user)
        return db_user


class Persons:
    def create_person(self, db: Session, person: schemas.Person):
        db_person = models.Person(**person.dict())
        db.add(db_person)
        db.commit()
        db.refresh(db_person)
        return db_person

    def read_all_persons(self, db: Session):
        return db.query(models.Person).all()

    @staticmethod
    def read_person(db: Session, tin: str):
        return db.query(models.Person).where(models.Person.tin == tin).first()


class Declarations:
    def get_declaration(self, db: Session, id: int):
        return db.query(models.Declaration).where(models.Declaration.id == id).first()

    def get_declarations(self, db: Session):
        x = db.query(models.Declaration).all()
        # print("The Object is: ", x[0].__dict__)
        return x

    def create_declaration(self, db: Session, declaration: schemas.DeclarationCreate):
        db_declaration = models.Declaration(
            **declaration.dict(),
            seller_acceptance=False,
            purchaser_acceptance=False,
            contract_details="",
            payment_method="",
        )
        db.add(db_declaration)
        db.commit()
        db.refresh(db_declaration)
        return db_declaration

    def update_declaration_acceptance(self, db: Session, id: int, tin: str):
        # TODO: DO NOT USE THIS WITH TIN, should be used with token
        # TODO: Integer literals for return values are kinda bad (?)

        declaration = Declarations.get_declaration(self, db, id)
        if not declaration:
            return {"status_code": 404, "detail": "Declaration not found"}
        if declaration.status == models.declaration.StatusEnum.completed:
            return {"status_code": 400, "detail": "Declaration is already completed"}

        who_accepts = {}
        if declaration.purchaser_tin == tin:
            if declaration.purchaser_acceptance:
                return {
                    "status_code": 400,
                    "detail": "Declaration is already accepted by person",
                }
            who_accepts = {"purchaser_acceptance": True}
        elif declaration.seller_tin == tin:
            if declaration.seller_acceptance:
                return {
                    "status_code": 400,
                    "detail": "Declaration is already accepted by person",
                }
            who_accepts = {"seller_acceptance": True}
        else:
            return {"status_code": 400, "detail": "Bad TIN"}

        stmt = (
            update(models.Declaration)
            .where(models.Declaration.id == id)
            .values(**who_accepts)
        )

        db.execute(statement=stmt)
        db.commit()
        db.refresh(declaration)
        return declaration

    def update_declaration_completion(self, db: Session, id: int, tin: str, data: schemas.DeclarationCompletion):
        declaration = Declarations.get_declaration(self, db, id)
        if not declaration:
            return {"status_code": 404, "detail": "Declaration not found"}
        if not declaration.notary_tin == tin:
            return {"status_code": 400, "detail": "Bad TIN"}
        if declaration.status == models.declaration.StatusEnum.completed:
            return {"status_code": 400, "detail": "Declaration is already completed"}
        if not (declaration.purchaser_acceptance and declaration.seller_acceptance):
            return {"status_code": 400, "detail": "Declaration is not accepted by all of the parties involved"}

        stmt = (
            update(models.Declaration)
            .where(models.Declaration.id == id)
            .values(**data.__dict__, status=schemas.DeclarationCreate.StatusEnum.completed)
        )
        db.execute(statement=stmt)
        db.commit()
        db.refresh(declaration)
        return declaration