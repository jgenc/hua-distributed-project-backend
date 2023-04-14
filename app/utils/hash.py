from datetime import timedelta, datetime
from passlib.context import CryptContext
from jose import JWTError, jwt

from ..schemas import TokenData

SECRET_KEY = "800fcff57e233658185e18bfd8f60f27bf28dcbf5d37193a8a2a9f10999b2441"
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = 30


pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")


def verify_password(plain_password, hashed_password):
    return pwd_context.verify(plain_password, hashed_password)


def get_password_hash(password):
    return pwd_context.hash(password)


def create_access_token(data: dict, expires_delta: timedelta | None = None):
    to_encode = data.copy()
    if expires_delta:
        expire = datetime.utcnow() + expires_delta
    else:
        expire = datetime.utcnow() + timedelta(minutes=15)
    to_encode.update({"exp": expire})
    encoded_jwt = jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
    return encoded_jwt


def get_token(token: str):
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        return TokenData(**payload)
    except JWTError:
        return None


def get_token_admin_role(token: str):
    try:
        payload: TokenData = TokenData(
            **jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        )
        return payload.admin == True
    except JWTError:
        return False


def get_token_notary_role(token: str):
    try:
        payload: TokenData = TokenData(
            **jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM])
        )
        return payload.notary == True
    except JWTError:
        return False
