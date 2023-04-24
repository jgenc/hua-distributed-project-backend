from sqlalchemy.orm import Session
from app import crud, schemas
from app.db import models
from app.core.settings import get_settings


def init_db(db: Session):
    admin = crud.users.read_user(db, get_settings().ADMIN_TIN)
    if not admin:
        admin_db = schemas.UserCreate(
            username=get_settings().ADMIN_USERNAME,
            password=get_settings().ADMIN_PASSWORD,
            tin=get_settings().ADMIN_TIN,
            role=schemas.RoleEnum.ROLE_ADMIN,
        )
        admin = crud.users.create_user(db, user=admin_db)
