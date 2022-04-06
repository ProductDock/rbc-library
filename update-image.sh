#!/bin/bash
docker stop rbc-library
docker rm rbc-library
docker rmi gcr.io/prod-pd-library/prod-pd-library-app-image:rbc-library
docker run -dp 8080:8080 --rm --name=rbc-library gcr.io/prod-pd-library/prod-pd-library-app-image:rbc-library