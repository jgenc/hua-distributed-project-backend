from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session

from ..dependencies import get_db, schemas, crud

router = APIRouter(prefix="/persons", tags=["persons"])
crud = crud.Persons()


@router.post("", response_model=schemas.Person)
async def create_person(person: schemas.Person, db: Session = Depends(get_db)):
    db_person = crud.read_person(db, person.tin)
    if db_person:
        raise HTTPException(status_code=400, detail="Person already registerd")
    return crud.create_person(db, person)


@router.get("", response_model=list[schemas.Person])
async def read_all_persons(db: Session = Depends(get_db)):
    return crud.read_all_persons(db)


@router.get("/{tin}", response_model=schemas.Person)
async def read_person(tin: str, db: Session = Depends(get_db)):
    if not tin.isdigit():
        raise HTTPException(status_code=400, detail="Bad TIN input")

    person = crud.read_person(db, tin)
    if not person:
        raise HTTPException(status_code=404, detail=f"Person with TIN: {tin} was not found")

    return person
