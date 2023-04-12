from pydantic import BaseModel
from .role import Role


class UserBase(BaseModel):
    tin: str
    username: str
    role: Role 


class UserCreate(UserBase):
    password: str


class User(UserBase):
    id: int

    class Config:
        orm_mode = True
