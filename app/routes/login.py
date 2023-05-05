from typing import Annotated
from fastapi import APIRouter, Depends, HTTPException, status
from fastapi.security import OAuth2PasswordRequestForm
from sqlalchemy.orm import Session

from ..dependencies import get_db, crud, utils, models, schemas, is_user_logged_in

router = APIRouter(prefix="/api/auth/login", tags=["auth"])


@router.post("", response_model=schemas.Token)
async def login(
    form_data: Annotated[OAuth2PasswordRequestForm, Depends()],
    db: Session = Depends(get_db),
):
    db_user = crud.auth.auth_user(db, form_data.username, form_data.password)
    if not db_user:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect username or password",
            headers={"WWW-Authenticate": "Bearer"},
        )
    token_data = schemas.TokenData(
        username=form_data.username,
        tin=db_user.tin,  # type: ignore
        admin=db_user.role.name == schemas.RoleEnum.ROLE_ADMIN,
        notary=db_user.role.name == schemas.RoleEnum.ROLE_NOTARY,
    )
    token = utils.token.create_access_token(token_data.dict())
    return schemas.Token(access_token=token, token_type="bearer")


@router.get("/refresh", response_model=schemas.Token)
async def refresh(
    token: Annotated[schemas.TokenData, Depends(is_user_logged_in)]
):
    refreshed_token = utils.token.create_access_token(token.dict())
    print("New Token: ", refreshed_token)
    return schemas.Token(access_token=refreshed_token, token_type="bearer") 
