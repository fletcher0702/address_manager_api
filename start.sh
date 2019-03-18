#!/bin/sh

mvn clean install

docker-compose down

docker-compose build

docker-compose up
