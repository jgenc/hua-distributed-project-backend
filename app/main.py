from fastapi import FastAPI
from .routes import users, declarations, persons

app = FastAPI()
app.include_router(users.router)
app.include_router(declarations.router)
app.include_router(persons.router)



@app.get("/")
def read_root():
    return {"msg": "hello"}
