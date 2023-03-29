from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session

from ..dependencies import get_db, schemas, crud

router = APIRouter(prefix="/declarations")
crud = crud.Declarations()


@router.get("", response_model=list[schemas.Declaration])
async def read_all_declarations(db: Session = Depends(get_db)):
    return crud.get_declarations(db)


@router.post("", response_model=schemas.Declaration)
async def create_declaration(
    declaration: schemas.Declaration, db: Session = Depends(get_db)
):
    return crud.create_declaration(db=db, declaration=declaration)
