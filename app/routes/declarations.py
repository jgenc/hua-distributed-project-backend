from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session

from ..dependencies import (
    get_db,
    schemas,
    crud,
    is_user_logged_in,
    is_user_notary,
)

from ..db.models.role import RoleEnum
from ..db.models.declaration import Declaration

router = APIRouter(
    prefix="/api/declarations",
    tags=["declarations"],
    dependencies=[Depends(is_user_logged_in)],
)
crud = crud.Declarations()


@router.post(
    "", response_model=schemas.DeclarationBase, dependencies=[Depends(is_user_notary)]
)
async def post_declaration(
    declaration: schemas.DeclarationCreate, db: Session = Depends(get_db)
):
    # TODO: Check if a declaration with the *exact* same details exist
    # meaning: purchaser, seller and notary tin
    # however, this does not really make any sense. The same guys could make
    # another deal again. For now we will let the backend to look *just*
    # at the declaration_id
    return crud.create_declaration(db=db, declaration=declaration)


@router.get("{id}", response_model=schemas.DeclarationBase | None)
async def get_declaration(id: int, db: Session = Depends(get_db)):
    # TODO: Check if provided token corresponds to any of the roles of the declaration
    declaration = crud.get_declaration(db, id)
    if not declaration:
        raise HTTPException(
            status_code=404, detail="Declaration with provided id was not found"
        )
    return declaration


# Debug route.
@router.get("", response_model=list[schemas.DeclarationBase])
async def get_all_declarations(db: Session = Depends(get_db)):
    return crud.get_declarations(db)


@router.get("", response_model=list[schemas.DeclarationBase])
async def get_declarations_person(db: Session = Depends(get_db)):
    # This should return the declarations that regard the logged in user.
    # This should look at the token of the logged in user, find out who it is
    # (should be in the token) and query the database based on the TIN of the
    # user.
    pass


@router.get("/role/{role}", response_model=list[schemas.DeclarationBase])
async def get_declaratons_role(role: RoleEnum, db: Session = Depends(get_db)):
    # TODO: Return all declarations based on the user AND the role provided
    # TBH, I do not rememember using this, I should research it
    pass


@router.post("/accept/{id}")
async def accept_declaration(id: int, tin: str, db: Session = Depends(get_db)):
    # TODO: Should not provide tin, this is for debug only
    res = crud.update_declaration_acceptance(db, id, tin)
    if not isinstance(res, Declaration):
        raise HTTPException(status_code=res["status_code"], detail=res["detail"])
    return res


@router.post("/complete/{id}", dependencies=[Depends(is_user_notary)])
async def complete_declaration(
    id: int,
    tin: str,
    data: schemas.DeclarationCompletion,
    db: Session = Depends(get_db),
):
    # TODO: No TIN required, should be done by token
    res = crud.update_declaration_completion(db, id, tin, data)
    if not isinstance(res, (Declaration)):
        raise HTTPException(status_code=res["status_code"], detail=res["detail"])
    return res
