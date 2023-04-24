from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.routes import users, declarations, persons, login

app = FastAPI()
app.include_router(users.router)
app.include_router(declarations.router)
app.include_router(persons.router)
app.include_router(login.router)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)