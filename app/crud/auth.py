from sqlalchemy.orm import Session
from app import models, utils


class Auth:
    def auth_user(self, db: Session, username: str, password: str):
        db_user: models.User = (
            db.query(models.User).where(models.User.username == username).first()
        )
        if not db_user:
            return False
        if not utils.token.verify_password(password, db_user.password):
            return False
        return db_user


auth = Auth()
