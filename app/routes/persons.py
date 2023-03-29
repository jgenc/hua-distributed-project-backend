from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session

from ..dependencies import get_db, schemas, crud

router = APIRouter(prefix="/persons")
crud = crud.Persons()


@router.get("", response_model=list[schemas.Person])
async def read_all_persons(db: Session = Depends(get_db)):
    return crud.get_persons(db)


@router.post("")
async def create_person(person: schemas.Person, db: Session = Depends(get_db)):
    return crud.create_person(db, person)
