FROM postgres:15.2
ENV POSTGRES_PASSWORD=dev
ENV PGDATA="/data"
VOLUME [ "/data" ]