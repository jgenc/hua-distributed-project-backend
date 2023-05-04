from pydantic import BaseModel, Field
from enum import Enum

from .user import tin_field
from .person import Person

# TODO: Find a way t make this field optional and also have restrictions
# if it has values
contract_details_field = Field(min_length=5, max_length=100)
payment_method_field = Field(min_length=4, max_length=20)


class StatusEnum(str, Enum):
    pending = "pending"
    completed = "completed"


class DeclarationCreate(BaseModel):
    seller_tin: str = tin_field
    purchaser_tin: str = tin_field
    property_number: str = Field(min_length=5, max_length=10)
    property_description: str = Field(min_length=5, max_length=100)
    tax: float = Field(min_size=0, max_size=1_000_000)

class DeclarationBase(BaseModel):
    id: int
    # notary_tin: str = tin_field

    notary: Person
    seller: Person
    purchaser: Person

    status: StatusEnum
    seller_acceptance: bool
    purchaser_acceptance: bool
    contract_details: str | None
    # TODO: Create an enum for this field. Both schemas and models.
    payment_method: str | None


    property_number: str = Field(min_length=5, max_length=10)
    property_description: str = Field(min_length=5, max_length=100)
    tax: float = Field(min_size=0, max_size=1_000_000)

    class Config:
        use_enum_values = True
        orm_mode = True

# class DeclarationBase(DeclarationCreate):
#     id: int
#     notary_tin: str = tin_field
#     status: StatusEnum
#     seller_acceptance: bool
#     purchaser_acceptance: bool
#     contract_details: str | None
#     # TODO: Create an enum for this field. Both schemas and models.
#     payment_method: str | None

#     class Config:
#         use_enum_values = True
#         orm_mode = True


class DeclarationCompletion(BaseModel):
    contract_details: str = contract_details_field
    payment_method: str = payment_method_field
