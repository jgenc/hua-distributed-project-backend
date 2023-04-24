# hua-distributed-project-backend

This is a repository used for the Distributed Systems course project and for the
DevOps course. To be more precise, this repository is the implementation of the
backend/API component for the "Property Management System" we had to implement.

## Technologies Used

The first edition of the project was implemented using the
[Spring](https://spring.io/) Java framework. You can find the source code for that
[here](https://github.com/jgenc/hua-distributed-project-backend/releases/tag/v1.0).
The second version will use the [fastapi](https://fastapi.tiangolo.com/) Python
framework and is currently being developed on the main branch.

In conjuction with **fastapi** we use:

1. A Postgres Database
2. [SQLAlchemy](https://www.sqlalchemy.org/) which is an ORM for Python
3. [Alembic](https://alembic.sqlalchemy.org/en/latest/) which handles migrations
4. Docker with *docker-compose*

We would also like to expand on the following topics:

1. Asynchronous Python methods and Asynchronous database operations
2. Deeper understanding of nginx (reverse proxies in general)
3. Image storage for future usage
4. Docker swarm/Kubernetes
5. CI/CD
