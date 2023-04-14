from sqlalchemy import Boolean, Column, ForeignKey, Integer, String, Float, Enum
from sqlalchemy.orm import relationship, Mapped, mapped_column
import enum

from ..database import Base
from .person import Person


class StatusEnum(str, enum.Enum):
    pending = "pending"
    completed = "completed"


class Declaration(Base):
    __tablename__ = "declarations"

    id = Column(Integer, primary_key=True, index=True, autoincrement="auto")
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
