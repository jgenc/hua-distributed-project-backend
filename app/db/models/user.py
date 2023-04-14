from sqlalchemy import Boolean, Column, ForeignKey, Integer, String, Float, Enum
from sqlalchemy.orm import relationship, Mapped, mapped_column
from typing import List, FrozenSet

from ..database import Base
from .role import Role

class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)
    username = Column(String(9), nullable=True, unique=True)
    password = Column(String, nullable=True)
    tin = Column(String(9), nullable=True, unique=True, index=True)

    role = relationship("Role", back_populates="user", uselist=False, cascade="all")