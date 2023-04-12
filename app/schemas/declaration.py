from pydantic import BaseModel
from enum import Enum


class StatusEnum(str, Enum):
    pending = "pending"
    completed = "completed"


class DeclarationCreate(BaseModel):
    notary_tin: str
    seller_tin: str
    purchaser_tin: str
    property_number: str
    property_description: str
    tax: float


class DeclarationBase(DeclarationCreate):
    id: int
    status: StatusEnum
    seller_acceptance: bool
    purchaser_acceptance: bool
    contract_details: str
    payment_method: str

    class Config:
        use_enum_values = True
        orm_mode = True


class DeclarationCompletion(BaseModel):
    contract_details: str
    payment_method: str
