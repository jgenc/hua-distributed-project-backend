#!/bin/sh

mode=$1
container=pg-project46

if [[ $mode == "start" ]]; then
	docker run --rm \
	--name $container \
	-e POSTGRES_PASSWORD=test \
	--net=host \
	-v $container-data:/var/lib/postgresql/data \
	-d postgres:11

	# instead of waiting for container status, i use a dumb sleep 
	sleep 8

	psql -h localhost -U postgres < create-developer-user.sql
	psql -h localhost -U developer -d taxdeclaration < taxdeclaration_init.sql
	psql -h localhost -U developer -d taxdeclaration < taxdeclaration_data.sql
elif [[ $mode == "ps" ]]; then
	exec docker ps
elif [[ $mode == "stop" ]]; then
	exec docker kill $container 
elif [[ $mode == "dvol" ]]; then
	exec docker volume rm $container-data 
else
	echo -e "Options:\n\tstart:\tstarts postgres docker container\n\tps:\tshows current running processes\n\tstop:\tstops $container\n\tdvol:\tdeletes volume"
fi
