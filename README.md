# hua-distributed-project-backend

### Run a postgres database using docker

```bash
docker run --name spb_db --rm -e  POSTGRES_PASSWORD=pass123 -e POSTGRES_DB=taxdeclaration --net=host -v pgdata14:/var/lib/postgresql/data  -d postgres:14
```

### Run init sql scripts
```bash
docker run --name spb_db --rm -e  POSTGRES_PASSWORD=pass123 -e POSTGRES_DB=taxdeclaration --net=host -v "$(pwd)"/assets/db:/docker-entrypoint-initdb.d -v pgdata14:/var/lib/postgresql/data -d postgres:14
```

## remove db data
```bash
docker volume rm pgdata14
```



Links:
* [install docker](https://tinyurl.com/2m3bhahn)
