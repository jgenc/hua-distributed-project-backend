from pydantic import BaseModel
from enum import Enum


class UserBase(BaseModel):
    tin: str
    username: str


class UserCreate(UserBase):
    password: str


class User(UserBase):
    id: int

    class Config:
        orm_mode = True


class Person(BaseModel):
    tin: str
    first_name: str
    last_name: str
    address: str
    doy: str

    class Config:
        orm_mode = True


class Declaration(BaseModel):
    class StatusEnum(str, Enum):
        pending = "pending"
        completed = "completed"

    id: int
    status: StatusEnum
    notary_tin: str
    seller_tin: str
    purchaser_tin: str
    property_number: str
    property_description: str
    tax: float
    seller_acceptance: bool
    purchaser_acceptance: bool
    contract_details: str
    payment_method: str

    class Config:
        use_enum_values = True
        orm_mode = True
