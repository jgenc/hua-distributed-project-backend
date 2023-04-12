from pydantic import BaseModel


class Person(BaseModel):
    tin: str
    first_name: str
    last_name: str
    address: str
    doy: str

    class Config:
        orm_mode = True
