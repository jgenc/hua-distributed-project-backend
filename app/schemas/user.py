from pydantic import BaseModel
from .role import Role, RoleEnum


class UserBase(BaseModel):
    tin: str
    username: str


class UserCreate(UserBase):
    password: str
    role: RoleEnum = RoleEnum.ROLE_CITIZEN


class User(UserBase):
    id: int
    role: Role 

    class Config:
        orm_mode = True

class UserUpdate(BaseModel):
    username: str | None = None
    tin: str | None = None
    role: RoleEnum | None = None