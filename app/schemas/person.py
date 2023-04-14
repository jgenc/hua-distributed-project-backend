from pydantic import BaseModel, Field
from .user import tin_field

first_name_field = Field(min_length=2, max_length=20)
last_name_field = Field(min_length=2, max_length=20)
address_field = Field(min_length=2, max_length=100)
doy_field = Field(min_length=5, max_length=25)

class PersonCreate(BaseModel):
    first_name: str = first_name_field
    last_name: str = last_name_field
    address: str = address_field
    doy: str = doy_field


class Person(PersonCreate):
    tin: str = tin_field

    class Config:
        orm_mode = True
