from sqlalchemy.orm import Session

from . import models, schemas


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
    def get_persons(self, db: Session):
        return db.query(models.Person).all()

    def create_person(self, db: Session, person: schemas.Person):
        db_person = models.Person(**person.dict())
        db.add(db_person)
        db.commit()
        db.refresh(db_person)
        return db_person


class Declarations:
    def get_declarations(self, db: Session):
        x = db.query(models.Declaration).all()
        # print("The Object is: ", x[0].__dict__)
        return x

    def create_declaration(self, db: Session, declaration: schemas.Declaration):
        db_declaration = models.Declaration(
            **declaration.dict(),
        )
        print("CREATING DECLARATION: ", db_declaration.__dict__)
        db.add(db_declaration)
        db.commit()
        db.refresh(db_declaration)
        return db_declaration
