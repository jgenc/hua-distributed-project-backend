from pydantic import BaseModel
from enum import Enum


class RoleEnum(str, Enum):
    ROLE_CITIZEN = "ROLE_CITIZEN"
    ROLE_NOTARY = "ROLE_NOTARY"
    ROLE_ADMIN = "ROLE_ADMIN"


class Role(BaseModel):
    name: RoleEnum

    class Config:
        orm_mode = True
