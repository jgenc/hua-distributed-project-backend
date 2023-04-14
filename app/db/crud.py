from sqlalchemy import update, select
from sqlalchemy.orm import Session, subqueryload, load_only

from . import models
from .. import schemas, utils

# TODO: Should I make the methods static?
# And even if I do, is there a point of creating the classes? Maybe a module
# would be better?


class Users:
    def read_user(self, db: Session, user_tin: str):
        return db.query(models.User).filter(models.User.tin == user_tin).first()

    def read_user_by_username(self, db: Session, username: str):
        return db.query(models.User).filter(models.User.username == username).first()

    def read_users(self, db: Session, skip: int = 0, limit: int = 10):
        query = (
            select(models.User)
            .options(subqueryload(models.User.role))
            .offset(skip)
            .limit(limit)
            .execution_options(populate_existing=True)
        )
        response = db.execute(query).scalars().all()
        return response

    def read_role(self, db: Session, user_id: int):
        return db.query(models.Role).filter(models.Role.id == user_id).first()

    def create_role(self, db: Session, id: int, role: schemas.RoleEnum):
        db_role = models.Role(id=id, name=role)
        return db_role

    def create_user(self, db: Session, user: schemas.UserCreate):
        hashed_password = utils.hash.get_password_hash(user.password)
        db_user = models.User(
            username=user.username,
            tin=user.tin,
            password=hashed_password,
        )
        db_role = Users.create_role(self, db=db, id=db_user.id, role=user.role)  # type: ignore
        db_user.role = db_role
        db.add(db_user)
        db.commit()
        db.refresh(db_user)
        return db_user

    # Here I get an object of model User because I already called the database
    # to check if a user exists. If everything is okay, just delete the user
    # and do not make another call
    def delete_user(self, db: Session, user: models.User):
        # TODO: The API doc says that the delete function should just return a
        # message. I find that dumb.
        db.delete(user)
        db.commit()
        return user

    def update_user(self, db: Session, user: models.User, user_update: dict):
        query = (
            update(models.User).where(models.User.id == user.id).values(**user_update)
        )
        db.execute(query)
        db.commit()
        return {"msg": "User updated successfully"}

    def update_user_password(self, db: Session, user: models.User, new_password: str):
        query = (
            update(models.User)
            .where(models.User.id == user.id)
            .values(password=new_password)
        )
        db.execute(query)
        db.commit()
        return {"msg": "Password updated successfully"}


class Persons:
    def create_person(self, db: Session, person: schemas.Person):
        db_person = models.Person(**person.dict())
        db.add(db_person)
        db.commit()
        db.refresh(db_person)
        return db_person

    def read_all_persons(self, db: Session):
        return db.query(models.Person).all()

    def read_person(self, db: Session, tin: str):
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
            status=models.declaration.StatusEnum.pending,
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

    def update_declaration_completion(
        self, db: Session, id: int, tin: str, data: schemas.DeclarationCompletion
    ):
        declaration = Declarations.get_declaration(self, db, id)
        if not declaration:
            return {"status_code": 404, "detail": "Declaration not found"}
        if not declaration.notary_tin == tin:
            return {"status_code": 400, "detail": "Bad TIN"}
        if declaration.status == models.declaration.StatusEnum.completed:
            return {"status_code": 400, "detail": "Declaration is already completed"}
        if not (declaration.purchaser_acceptance and declaration.seller_acceptance):
            return {
                "status_code": 400,
                "detail": "Declaration is not accepted by all of the parties involved",
            }

        stmt = (
            update(models.Declaration)
            .where(models.Declaration.id == id)
            .values(**data.__dict__, status=schemas.declaration.StatusEnum.completed)
        )
        db.execute(statement=stmt)
        db.commit()
        db.refresh(declaration)
        return declaration


class Auth:
    def auth_user(self, db: Session, username: str, password: str):
        db_user: models.User = (
            db.query(models.User).where(models.User.username == username).first()
        )
        if not db_user:
            return False
        if not utils.hash.verify_password(password, db_user.password):
            return False
        return db_user
