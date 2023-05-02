from sqlalchemy import update, select
from sqlalchemy.orm import Session, subqueryload

from app import models, schemas, utils


class Users:
    def __user_serializer(self, user: models.User):
        return {
            "id": user.id,
            "tin": user.tin,
            "username": user.username,
            "role": user.role.name,
        }

    def read_user(self, db: Session, user_tin: str):
        return db.query(models.User).filter(models.User.tin == user_tin).first()

    def read_user_by_username(self, db: Session, username: str):
        return db.query(models.User).filter(models.User.username == username).first()

    # FIXME: limit should be 10. Pagination in frontend is required
    def read_users(self, db: Session, skip: int = 0, limit: int = 50):
        query = (
            select(models.User)
            .options(subqueryload(models.User.role).load_only(models.Role.name))
            .offset(skip)
            .limit(limit)
            .execution_options(populate_existing=True)
        )
        response = db.execute(query).scalars().all()
        serialized_response = [self.__user_serializer(user) for user in response]
        return serialized_response 

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
        db_user.role = models.Role(id=db_user.id, name=user.role)
        db.add(db_user)
        db.commit()
        db.refresh(db_user)
        return self.__user_serializer(db_user)

    # Here I get an object of model User because I already called the database
    # to check if a user exists. If everything is okay, just delete the user
    # and do not make another call
    def delete_user(self, db: Session, user: models.User):
        # TODO: The API doc says that the delete function should just return a
        # message. I find that dumb.
        db.delete(user)
        db.commit()
        return user

    def update_user(
        self, db: Session, user: models.User, user_update: schemas.UserUpdate
    ):
        if user_update.username or user_update.tin:
            query = (
                update(models.User)
                .where(models.User.id == user.id)
                .values(**user_update.dict(exclude_unset=True, exclude={"role"}))
            )
            db.execute(query)
            db.commit()
        if user_update.role:
            query = (
                update(models.Role)
                .where(models.Role.id == user.id)
                .values(name=user_update.role)
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
