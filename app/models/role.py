from sqlalchemy import Boolean, Column, ForeignKey, Integer, String, Float, Enum
from sqlalchemy.orm import relationship, Mapped, mapped_column
import enum
from typing import List, FrozenSet

from ..db.database import Base

class RoleEnum(str, enum.Enum):
    ROLE_CITIZEN = "ROLE_CITIZEN"
    ROLE_NOTARY = "ROLE_NOTARY"
    ROLE_ADMIN = "ROLE_ADMIN"


class Role(Base):
    __tablename__ = "roles"

    id: Mapped[int] = mapped_column(ForeignKey("users.id"), primary_key=True)
    name: Mapped[RoleEnum] = mapped_column()
    user = relationship("User", back_populates="role", uselist=False, cascade="all")