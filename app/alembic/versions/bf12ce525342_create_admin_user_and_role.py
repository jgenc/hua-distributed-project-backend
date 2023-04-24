"""create admin user and role

Revision ID: bf12ce525342
Revises: 
Create Date: 2023-04-16 11:45:34.357879

"""
from alembic import op
import sqlalchemy as sa
from sqlalchemy import Table

from dotenv import get_key

ADMIN_USERNAME = get_key("../.env", "ADMIN_USERNAME")
ADMIN_PASSWORD = get_key("../.env", "ADMIN_PASSWORD")
ADMIN_TIN = 0
ADMIN_ID = 0

# revision identifiers, used by Alembic.
revision = "bf12ce525342"
down_revision = None
branch_labels = None
depends_on = None


def upgrade() -> None:
    bind = op.get_bind()
    role = Table("roles", sa.MetaData(), autoload_with=bind)
    user = Table("users", sa.MetaData(), autoload_with=bind)

    op.bulk_insert(
        user,
        [
            {
                "id": ADMIN_ID,
                "username": ADMIN_USERNAME,
                "password": ADMIN_PASSWORD,
                "tin": ADMIN_TIN,
            }
        ],
    )
    op.bulk_insert(role, [{"id": ADMIN_ID, "name": "ROLE_ADMIN"}])

def downgrade() -> None:
    op.execute("DELETE FROM roles WHERE id = 0")
    op.execute("DELETE FROM users WHERE id = 0")
    pass
