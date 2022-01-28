ARG keycloak_version

FROM jboss/keycloak:${keycloak_version}

ARG keycloak_bcrypt_version=1.5.0

COPY ./keycloak-bcrypt-${keycloak_bcrypt_version}.jar /opt/jboss/keycloak/standalone/deployments/
