from sqlalchemy.orm import Session

from app import models, schemas

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

persons = Persons()