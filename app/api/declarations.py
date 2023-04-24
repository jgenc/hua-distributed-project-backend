from typing import Annotated
from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session

from ..dependencies import (
    get_db,
    schemas,
    crud,
    is_user_logged_in,
    is_user_notary,
    is_simple_user,
)

from ..models.role import RoleEnum
from ..models.declaration import Declaration

router = APIRouter(
    prefix="/api/declarations",
    tags=["declarations"],
    dependencies=[Depends(is_simple_user)],
)
crud = crud.Declarations()


@router.post(
    "", response_model=schemas.DeclarationBase, dependencies=[Depends(is_user_notary)]
)
async def post_declaration(
    declaration: schemas.DeclarationCreate,
    token: Annotated[schemas.TokenData, Depends(is_user_logged_in)],
    db: Session = Depends(get_db),
):
    res = crud.create_declaration(db=db, declaration=declaration, notary_tin=token.tin)
    if not isinstance(res, Declaration):
        raise HTTPException(status_code=res["status_code"], detail=res["detail"])
    return res


@router.get("/{id}", response_model=schemas.DeclarationBase | None)
async def get_declaration(
    id: int,
    token: Annotated[schemas.TokenData, Depends(is_user_logged_in)],
    db: Session = Depends(get_db),
):
    declaration = crud.get_declaration(db, id)
    if not declaration:
        raise HTTPException(status_code=404, detail="Wrong ID or wrong TIN")
    return declaration


@router.get("", response_model=list[schemas.DeclarationBase])
async def get_declarations_person(
    token: Annotated[schemas.TokenData, Depends(is_user_logged_in)],
    db: Session = Depends(get_db),
):
    return crud.get_person_declarations(db, token.tin)


@router.get("/role/{role}", response_model=list[schemas.DeclarationBase])
async def get_declaratons_role(role: RoleEnum, db: Session = Depends(get_db)):
    # TODO: Return all declarations based on the user AND the role provided
    # TBH, I do not rememember using this, I should research it
    pass


@router.post("/accept/{id}")
async def accept_declaration(
    id: int,
    token: Annotated[schemas.TokenData, Depends(is_user_logged_in)],
    db: Session = Depends(get_db),
):
    res = crud.update_declaration_acceptance(db, id, token.tin)
    if not isinstance(res, Declaration):
        raise HTTPException(status_code=res["status_code"], detail=res["detail"])
    return res


@router.post("/complete/{id}", dependencies=[Depends(is_user_notary)])
async def complete_declaration(
    id: int,
    token: Annotated[schemas.TokenData, Depends(is_user_logged_in)],
    data: schemas.DeclarationCompletion,
    db: Session = Depends(get_db),
):
    res = crud.update_declaration_completion(db, id, token.tin, data)
    if not isinstance(res, (Declaration)):
        raise HTTPException(status_code=res["status_code"], detail=res["detail"])
    return res
