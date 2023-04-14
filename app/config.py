from pydantic import BaseSettings


class Settings(BaseSettings):
    SQLALCHEMY_DATABASE_URI: str = "NOTHING"

    class Config:
        env_file = ".env"
