from sqlalchemy import Boolean, Column, ForeignKey, Integer, String, Float, Enum
from sqlalchemy.orm import relationship, Mapped
from typing import List, FrozenSet

from ..database import Base

# I do not import Declaration again, as it will be a cyclical import and that 
# would make no sense. However, pylance will be a bitch about it.

class Person(Base):
    __tablename__ = "persons"

    tin = Column(String(9), primary_key=True, nullable=True, unique=True)
    first_name = Column("first_name", String(30))
    last_name = Column("last_name", String(30))
    address = Column("address", String(50))
    doy = Column(String(20))

    notary_declarations: Mapped[List["Declaration"]] = relationship(
        back_populates="notary", foreign_keys="[Declaration.notary_tin]"
    )
    seller_declarations: Mapped[List["Declaration"]] = relationship(
        back_populates="seller", foreign_keys="[Declaration.seller_tin]"
    )
    purchaser_declarations: Mapped[List["Declaration"]] = relationship(
        back_populates="purchaser", foreign_keys="[Declaration.purchaser_tin]"
    )
