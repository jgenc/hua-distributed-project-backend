from typing import Annotated
from fastapi import Depends
from fastapi.security import OAuth2PasswordBearer

from .db.database import SessionLocal, engine
from .db import crud, models

from . import schemas, utils

models.Base.metadata.create_all(bind=engine)

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="/api/auth/login")


def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


async def is_user_admin(token: Annotated[str, Depends(oauth2_scheme)]):
    return utils.hash.get_token_admin_role(token)


async def is_user_notary(token: Annotated[str, Depends(oauth2_scheme)]):
    return utils.hash.get_token_notary_role(token)


async def is_user_logged_in(token: Annotated[str, Depends(oauth2_scheme)]):
    return utils.hash.get_token(token)
