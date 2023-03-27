from .sql_app.database import SessionLocal, engine
from .sql_app import crud, models, schemas

models.Base.metadata.create_all(bind=engine)


def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()
