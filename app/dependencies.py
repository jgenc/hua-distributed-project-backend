from .db.database import SessionLocal, engine
from .db import crud, models

from . import schemas

models.Base.metadata.create_all(bind=engine)


def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()
