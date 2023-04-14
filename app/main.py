from fastapi import FastAPI
from .routes import users, declarations, persons, login

app = FastAPI()
app.include_router(users.router)
app.include_router(declarations.router)
app.include_router(persons.router)
app.include_router(login.router)
