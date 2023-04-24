from sqlalchemy import update, select
from sqlalchemy.orm import Session, subqueryload

from app import models, schemas, utils


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
        hashed_password = utils.token.get_password_hash(user.password)
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

users = Users()