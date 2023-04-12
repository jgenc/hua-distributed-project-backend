from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session

from ..dependencies import get_db, schemas, crud


router = APIRouter(prefix="/admin/user", tags=["admin"])
crud = crud.Users()


@router.post("")
async def create_user(user: schemas.UserCreate, db: Session = Depends(get_db)):
    return crud.create_user(db=db, user=user)


@router.get("", response_model=list[schemas.User])
async def read_all_users(db: Session = Depends(get_db)):
    return crud.get_users(db=db)
