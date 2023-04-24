from pydantic import BaseSettings


class Settings(BaseSettings):
    SQLALCHEMY_DATABASE_URI: str
    ADMIN_USERNAME: str
    ADMIN_PASSWORD: str
    ADMIN_ID: int = 0
    ADMIN_TIN: str = "000000000"

    class Config:
        env_file = ".env"
