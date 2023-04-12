from pydantic import BaseModel
from enum import Enum

class DeclarationCreate(BaseModel):
    class StatusEnum(str, Enum):
        pending = "pending"
        completed = "completed"

    # FIXME: Do we actually need id in here?

    id: int
    status: StatusEnum
    notary_tin: str
    seller_tin: str
    purchaser_tin: str
    property_number: str
    property_description: str
    tax: float


class Declaration(DeclarationCreate):
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