from pydantic import BaseModel


class PersonCreate(BaseModel):
    first_name: str
    last_name: str
    address: str
    doy: str


class Person(PersonCreate):
    tin: str

    class Config:
        orm_mode = True
