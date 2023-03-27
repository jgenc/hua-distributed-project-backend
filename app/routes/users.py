from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session

from ..dependencies import get_db, schemas, crud

router = APIRouter(prefix="/users")


@router.post("")
async def create_user(user: schemas.UserCreate, db: Session = Depends(get_db)):
    return crud.create_user(db=db, user=user)
