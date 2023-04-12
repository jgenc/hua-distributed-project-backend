from sqlalchemy import Boolean, Column, ForeignKey, Integer, String, Float, Enum
from sqlalchemy.orm import relationship, Mapped, mapped_column
import enum
from typing import List, FrozenSet

from ..database import Base

class RoleEnum(str, enum.Enum):
    ROLE_CITIZEN = "ROLE_CITIZEN"
    ROLE_NOTARY = "ROLE_NOTARY"
    ROLE_ADMIN = "ROLE_ADMIN"


class Role(Base):
    __tablename__ = "roles"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(Enum(RoleEnum))
