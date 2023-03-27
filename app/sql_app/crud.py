from sqlalchemy.orm import Session

from . import models, schemas


def get_user(db: Session, user_id: int):
    return db.query(models.User).filter(models.User.id == user_id).first()


def get_users(db: Session, skip: int = 0, limit: int = 10):
    return db.query(models.User).offset(skip).limit(limit).all()


def create_user(db: Session, user: schemas.UserCreate):
    not_really_hashed = user.password + "_hashed"
    db_user = models.User(
        password=not_really_hashed, username=user.username, tin=user.tin
    )
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    return db_user
