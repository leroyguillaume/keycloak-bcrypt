#!/bin/bash

set -e

VERSION=$(./gradlew properties -q | grep version: | awk '{print $2}')

cp build/libs/keycloak-bcrypt-$VERSION.jar docker/
docker-compose up -d
sleep 60 # Waiting for Keycloak
docker-compose exec keycloak /opt/jboss/keycloak/bin/add-user-keycloak.sh -u admin -p admin
docker-compose exec keycloak cp /mnt/keycloak-bcrypt-$VERSION.jar /opt/jboss/keycloak/standalone/deployments/keycloak-bcrypt-$VERSION.jar
docker-compose restart keycloak
