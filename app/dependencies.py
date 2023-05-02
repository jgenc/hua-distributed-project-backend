from typing import Annotated
from fastapi import Depends, HTTPException, status
from fastapi.security import OAuth2PasswordBearer

from .db.database import SessionLocal, engine
from app import crud, models

from . import schemas, utils

models.Base.metadata.create_all(bind=engine)

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="/api/auth/login")


def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


async def is_user_logged_in(token: Annotated[str, Depends(oauth2_scheme)]):
    token_data: utils.token.TokenData | None = utils.token.get_token(token)
    if not token_data:
        raise HTTPException(status.HTTP_401_UNAUTHORIZED)
    return token_data


async def is_simple_user(
    user: Annotated[schemas.TokenData, Depends(is_user_logged_in)]
):
    if user.admin:
        raise HTTPException(status.HTTP_403_FORBIDDEN)
    return True


async def is_user_admin(user: Annotated[schemas.TokenData, Depends(is_user_logged_in)]):
    if not user.admin:
        raise HTTPException(status.HTTP_403_FORBIDDEN)
    return True


async def is_user_notary(
    user: Annotated[schemas.TokenData, Depends(is_user_logged_in)]
):
    print(user)
    if not user.notary:
        raise HTTPException(status.HTTP_403_FORBIDDEN)
    return True
