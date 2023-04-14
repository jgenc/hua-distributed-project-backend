from pydantic import BaseModel, Field
from pydantic.generics import GenericModel
from .role import Role, RoleEnum

tin_field = Field(min_length=9, max_length=9)
username_field = Field(min_length=4, max_length=20)
password_field = Field(min_length=8, max_length=32)


class UserBase(BaseModel):
    tin: str = tin_field
    username: str = username_field


class UserCreate(UserBase):
    password: str = password_field
    role: RoleEnum = RoleEnum.ROLE_CITIZEN


class User(UserBase):
    id: int
    role: Role

    class Config:
        orm_mode = True


class UserUpdate(BaseModel):
    tin: str | None = tin_field
    username: str | None = username_field
    role: RoleEnum | None = None


class UserLogin(BaseModel):
    username: str = username_field
    password: str = password_field
