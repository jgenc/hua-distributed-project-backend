from sqlalchemy import update
from sqlalchemy.orm import Session, subqueryload
from app import models, schemas
from app.crud import persons


class Declarations:
    PERSON_DOES_NOT_EXIST = lambda tin: {
        "status_code": 404,
        "detail": f"Person with TIN {tin} does not exist",
    }

    IS_DECLARATION_RELEVANT = lambda tin: (
        (models.Declaration.notary_tin == tin)
        | (models.Declaration.purchaser_tin == tin)
        | (models.Declaration.seller_tin == tin)
    )

    def get_declaration(self, db: Session, id: int):
        return db.query(models.Declaration).where(models.Declaration.id == id).first()

    def get_person_declarations(self, db: Session, tin: str):
        return (
            db.query(models.Declaration)
            .where(Declarations.IS_DECLARATION_RELEVANT(tin))
            .all()
        )

    def create_declaration(
        self, db: Session, declaration: schemas.DeclarationCreate, notary_tin: str
    ):
        if declaration.seller_tin == declaration.purchaser_tin:
            return {"status_code": 400, "detail": "Seller and purchaser are the same"}

        for tin in [
            declaration.purchaser_tin,
            declaration.seller_tin,
        ]:
            if not persons.read_person(db, tin):
                return Declarations.PERSON_DOES_NOT_EXIST(tin)

        db_declaration = models.Declaration(
            **declaration.dict(),
            notary_tin=notary_tin,
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
            .values(**data.__dict__, status=schemas.StatusEnum.completed)
        )
        db.execute(statement=stmt)
        db.commit()
        db.refresh(declaration)
        return declaration


declarations = Declarations()
