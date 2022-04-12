#!/bin/bash
sudo usermod -a -G docker ${USER}
docker-credential-gcr configure-docker
docker stop rbc-library
docker rm rbc-library
docker rmi gcr.io/prod-pd-library/prod-pd-library-app-image:rbc-library
docker run -v $GOOGLE_APPLICATION_CREDENTIALS:/home/credentials.json:ro -e GOOGLE_APPLICATION_CREDENTIALS=/home/pd-library/credentials.json -dp 8080:8080 --name=rbc-library gcr.io/prod-pd-library/prod-pd-library-app-image:rbc-library
docker container ls -a