from sqlalchemy import Boolean, Column, ForeignKey, Integer, String, Float, Enum
from sqlalchemy.orm import relationship, Mapped, mapped_column
import enum
from typing import List, FrozenSet

from .database import Base


class StatusEnum(str, enum.Enum):
    pending = "pending"
    completed = "completed"


class RoleEnum(str, enum.Enum):
    ROLE_CITIZEN = "ROLE_CITIZEN"
    ROLE_NOTARY = "ROLE_NOTARY"
    ROLE_ADMIN = "ROLE_ADMIN"


class Role(Base):
    __tablename__ = "roles"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(Enum(RoleEnum))


class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)
    username = Column(String, nullable=True, unique=True)
    password = Column(String, nullable=True)
    tin = Column(String(9), nullable=True, unique=True)


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


class Declaration(Base):
    __tablename__ = "declarations"

    id = Column(Integer, primary_key=True, index=True)
    status = Column(Enum(StatusEnum))

    notary_tin: Mapped[String] = mapped_column(ForeignKey("persons.tin"))
    seller_tin: Mapped[String] = mapped_column(ForeignKey("persons.tin"))
    purchaser_tin: Mapped[String] = mapped_column(ForeignKey("persons.tin"))

    notary: Mapped["Person"] = relationship(
        back_populates="notary_declarations", foreign_keys=[notary_tin]
    )
    seller: Mapped["Person"] = relationship(
        back_populates="seller_declarations", foreign_keys=[seller_tin]
    )
    purchaser: Mapped["Person"] = relationship(
        back_populates="purchaser_declarations", foreign_keys=[purchaser_tin]
    )

    property_number = Column("property_number", String(5))
    property_description = Column("property_description", String(255))
    tax = Column(Float)
    seller_acceptance = Column("seller_acceptance", Boolean)
    purchaser_acceptance = Column("purchaser_acceptance", Boolean)
    contract_details = Column("contract_details", String(25))
    # TODO: Make this do something
    payment_method = Column("payment_method", String(10))
