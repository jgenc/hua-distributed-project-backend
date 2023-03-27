from pydantic import BaseModel

class Person(BaseModel):
  tin: str
  first_name: str
  last_name: str
  address: str | None = None
  doy: str | None = None
  notary_declarations: set | None = None
  purchaser_declarations: set | None = None