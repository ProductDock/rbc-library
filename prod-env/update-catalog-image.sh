#!/bin/bash
sudo usermod -a -G docker ${USER}
docker-credential-gcr configure-docker

docker stop rbc-library-catalog
docker rm rbc-library-catalog
docker rmi $(docker images | grep "rbc-library-catalog")

export GOOGLE_APPLICATION_CREDENTIALS=/home/pd-library/credentials.json
docker run -v $GOOGLE_APPLICATION_CREDENTIALS:/home/credentials.json:ro -e GOOGLE_APPLICATION_CREDENTIALS=/home/credentials.json -dp 8080:8080 --name=rbc-library-catalog gcr.io/prod-pd-library/rbc-library-catalog:$1
docker container ls -a