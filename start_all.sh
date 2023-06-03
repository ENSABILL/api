#!/bin/bash

export HOST_UID="$(id -u)"

export HOST_GID="$(id -g)"

docker-compose down

docker-compose up --build -d backend mysql phpmyadmin
