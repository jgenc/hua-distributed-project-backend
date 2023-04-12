from sqlalchemy import Boolean, Column, ForeignKey, Integer, String, Float, Enum
from sqlalchemy.orm import relationship, Mapped, mapped_column
import enum
from typing import List, FrozenSet

from ..database import Base

class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)
    username = Column(String, nullable=True, unique=True)
    password = Column(String, nullable=True)
    tin = Column(String(9), nullable=True, unique=True)