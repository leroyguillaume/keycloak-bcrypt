# Keycloak BCrypt

Add a password hash provider to handle BCrypt passwords inside Keycloak.

## Build JAR

```bash
./gradlew assemble -Pdependency.keycloak.version=${KEYCLOAK_VERSION}
```

## Build Docker image

```bash
docker build \
    --build-arg keycloak_version=${KEYCLOAK_VERSION} \
    -t gleroy/keycloak-bcrypt \
    .
```

## Test with docker-compose

```bash
docker-compose up -d
```

## Install

### >= 17.0.0

```bash
curl -L https://github.com/leroyguillaume/keycloak-bcrypt/releases/download/${KEYCLOAK_BCRYPT_VERSION}/keycloak-bcrypt-${KEYCLOAK_BCRYPT_VERSION}.jar > ${KEYCLOAK_HOME}/providers/keycloak-bcrypt-${KEYCLOAK_BCRYPT_VERSION}.jar
```
You need to restart Keycloak.

### < 17.0.0

```bash
curl -L https://github.com/leroyguillaume/keycloak-bcrypt/releases/download/${KEYCLOAK_BCRYPT_VERSION}/keycloak-bcrypt-${KEYCLOAK_BCRYPT_VERSION}.jar > ${KEYCLOAK_HOME}/standalone/deployments/keycloak-bcrypt-${KEYCLOAK_BCRYPT_VERSION}.jar
```
You need to restart Keycloak.

## Run with Docker

```bash
docker run \
    -e KEYCLOAK_ADMIN=${KEYCLOAK_ADMIN} \
    -e KEYCLOAK_ADMIN_PASSWORD=${KEYCLOAK_ADMIN_PASSWORD} \
    -e KC_HOSTNAME=${KC_HOSTNAME} \
    gleroy/keycloak-bcrypt \
    start
```

The image is based on [Keycloak official](https://quay.io/repository/keycloak/keycloak) one.

## How to use
Go to `Authentication` / `Policies` / `Password policy` and add hashing algorithm policy with value `bcrypt`.

To test if installation works, create new user and set its credentials.
