from pydantic import BaseModel


class UserBase(BaseModel):
    tin: str
    username: str


class UserCreate(UserBase):
    password: str


class User(UserBase):
    id: int

    class Config:
        orm_mode = True
