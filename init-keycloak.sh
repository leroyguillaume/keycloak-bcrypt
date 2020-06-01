#!/bin/bash

set -e

VERSION=$(mvn org.apache.maven.plugins:maven-help-plugin:3.1.0:evaluate -Dexpression=project.version -q -DforceStdout)

cp target/keycloak-bcrypt-$VERSION.jar docker/
docker-compose up -d
sleep 60 # Waiting for Keycloak
docker-compose exec keycloak /opt/jboss/keycloak/bin/add-user-keycloak.sh -u admin -p admin
docker-compose exec keycloak /opt/jboss/keycloak/bin/jboss-cli.sh --command="module add --name=org.mindrot.jbcrypt --resources=/mnt/jbcrypt-0.4.jar"
docker-compose exec keycloak cp /mnt/keycloak-bcrypt-$VERSION.jar /opt/jboss/keycloak/standalone/deployments/keycloak-bcrypt-$VERSION.jar
docker-compose restart keycloak
