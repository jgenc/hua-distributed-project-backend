from app.db.init_db import init_db
from app.db.database import SessionLocal

def init():
    db = SessionLocal()
    init_db(db)

def main():
    init()

if __name__ == "__main__":
    main()