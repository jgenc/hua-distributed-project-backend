from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session

from ..dependencies import get_db, schemas, crud, is_user_admin

from app.models import User
from app.utils import token


router = APIRouter(
    prefix="/api/admin/user", tags=["admin"], dependencies=[Depends(is_user_admin)]
)


@router.post("", response_model=schemas.User)
async def post_user(user: schemas.UserCreate, db: Session = Depends(get_db)):
    if user.role == "ROLE_ADMIN":
        raise HTTPException(status_code=400, detail="Cannot create user")
    existing_db_user = crud.users.read_user(db, user.tin)
    if existing_db_user:
        raise HTTPException(
            status_code=400, detail="User with provided TIN already exists"
        )
    existing_db_user = crud.users.read_user_by_username(db, user.username)
    if existing_db_user:
        raise HTTPException(
            status_code=400, detail="User with provided username already exists"
        )

    res = crud.users.create_user(db=db, user=user)
    # FIXME
    # if not isinstance(res, User):
    #     raise HTTPException(status_code=res["status_code"], detail=res["detail"])
    return res


@router.get("", response_model=list[schemas.User])
async def get_all_users(db: Session = Depends(get_db)):
    all_users = crud.users.read_users(db=db)
    # admin_user = crud.users.read_user_by_username(db, "admin")
    # if admin_user in all_users:
    #     all_users.remove(admin_user)
    return all_users


@router.get("/{tin}", response_model=schemas.User)
async def get_user(tin: str, db: Session = Depends(get_db)):
    db_user = crud.users.read_user(db, tin)
    if db_user is None:
        raise HTTPException(status_code=404, detail="User not found")
    return db_user


@router.delete("/{tin}", response_model=schemas.User)
async def delete_user(tin: str, db: Session = Depends(get_db)):
    db_user = crud.users.read_user(db, tin)
    if db_user is None:
        raise HTTPException(status_code=404, detail="User not found")
    return crud.users.delete_user(db, db_user)


@router.put("/{tin}")
async def update_user(
    tin: str, user: schemas.UserUpdate, db: Session = Depends(get_db)
):
    db_user = crud.users.read_user(db, tin)
    if db_user is None:
        raise HTTPException(status_code=404, detail="User not found")
    updated_user = {}
    if user.username:
        updated_user.update({"username": user.username})
    if user.tin:
        updated_user.update({"tin": user.tin})
    if user.role:
        updated_user.update({"role": user.role.name})
    update_user = schemas.UserUpdate(**updated_user)
    return crud.users.update_user(db, db_user, update_user)


@router.post("/{tin}")
async def update_user_password(
    tin: str, new_password: dict, db: Session = Depends(get_db)
):
    db_user = crud.users.read_user(db, tin)
    if db_user is None:
        raise HTTPException(status_code=404, detail="User not found")

    
    return crud.users.update_user_password(
        db, db_user, token.get_password_hash(new_password.get("password"))
    )
