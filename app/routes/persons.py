from typing import Annotated
from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session

from ..dependencies import (
    get_db,
    schemas,
    crud,
    is_user_notary,
    is_user_logged_in,
)

router = APIRouter(prefix="/api/persons", tags=["persons"])


@router.post("", response_model=schemas.Person)
async def create_person(
    person: schemas.PersonCreate,
    token_data: Annotated[schemas.TokenData, Depends(is_user_logged_in)],
    db: Session = Depends(get_db),
):
    if token_data.admin:
        raise HTTPException(status_code=403, detail="Forbidden")

    db_person = crud.persons.read_person(db, token_data.tin)
    if db_person:
        raise HTTPException(status_code=400, detail="Person already exists")
    db_person = crud.persons.create_person(
        db, schemas.Person(**person.dict(), tin=token_data.tin)
    )
    return db_person


@router.get(
    "", response_model=list[schemas.Person], dependencies=[Depends(is_user_notary)]
)
async def read_all_persons(db: Session = Depends(get_db)):
    return crud.persons.read_all_persons(db)


@router.get("/{tin}", response_model=schemas.Person | None)
async def read_person(
    tin: str,
    token_data: Annotated[schemas.TokenData, Depends(is_user_logged_in)],
    db: Session = Depends(get_db),
):
    if not tin.isdigit():
        raise HTTPException(status_code=400, detail="Bad TIN input")

    if not token_data.tin == tin and not token_data.notary:
        raise HTTPException(status_code=403, detail="Forbidden")

    if token_data.tin == tin:
        return crud.persons.read_person(db, tin)

    person = crud.persons.read_person(db, tin)
    if not person:
        raise HTTPException(status_code=404, detail=f"Person was not found")

    return person
