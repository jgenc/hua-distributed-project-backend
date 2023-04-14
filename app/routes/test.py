from typing import Annotated
from datetime import datetime, timedelta
from fastapi import APIRouter, Depends, HTTPException, status
from pydantic import BaseModel
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm
from passlib.context import CryptContext
from jose import JWTError, jwt

SECRET_KEY = "3491d24119ff6c99b1f4e9390fa4ba0a95f73fc0d3d6c18f6a1a1fb7404c1e14"
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = 30

fake_users_db = {
    "test": {
        "username": "test",
        "hashed_password": "$2b$12$E3/d27oy1SPiAO29ABusv.LNKpL4kdORGBjBE5B6i8BP9N2nBu4De",
        "email": "test@test.com",
    },
    "admin": {
        "username": "admin",
        "hashed_password": "$2b$12$.st/V/kyZcEyGAmYHuSi9OQsFCGq.Ey4tYMIMu7IxXb7P3WO.6goS",
        "email": "test@test.com",
    },
}

router = APIRouter(prefix="/test")

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="/test/login")
pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")


class Token(BaseModel):
    access_token: str
    token_type: str


class TokenData(BaseModel):
    username: str | None = None


def verify_password(plain_password, hashed_password):
    return pwd_context.verify(plain_password, hashed_password)


def get_password_hash(password):
    return pwd_context.hash(password)


def create_access_token(data: dict, expires_delta: timedelta | None = None):
    to_encode = data.copy()
    if expires_delta:
        expire = datetime.utcnow() + expires_delta
    else:
        # default expire time if CONSTANT is not given
        expire = datetime.utcnow() + timedelta(minutes=15)
    to_encode.update({"exp": expire})
    encoded_jwt = jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
    return encoded_jwt


def get_user(db, username: str):
    if username in db:
        user_dict = db[username]
        # here should return an user from the databse
        return user_dict


def authenticate_user(db, username: str, password: str):
    # retrieve user from database and his hashed password
    user = get_user(db, username)

    if not user:
        return False
    if not verify_password(password, user.get("hashed_password")):
        return False
    return user


@router.post("/login")
async def login_for_token(form_data: Annotated[OAuth2PasswordRequestForm, Depends()]):
    user = authenticate_user(fake_users_db, form_data.username, form_data.password)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            headers={"WWW-Authenticate": "Bearer"},
        )
    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = create_access_token(
        data={"sub": user.get("username")}, expires_delta=access_token_expires
    )
    return {"access_token": access_token, "token_type": "bearer"}


@router.get("/action")
async def test(token: Annotated[str, Depends(oauth2_scheme)]):
    unauthorized = HTTPException(
        status_code=status.HTTP_401_UNAUTHORIZED,
        headers={"WWW-Authenticate": "Bearer"},
    )

    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        username = payload.get("sub")
        if username is None:
            raise unauthorized
        token_data = TokenData(username=username)
    except JWTError:
        raise unauthorized

    user = get_user(fake_users_db, username=token_data.username)
    if user is None:
        raise unauthorized
    return user
