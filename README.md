# Keycloak SHA-1

A minimal fork of Keycloak BCrypt to also support passwords hashed with SHA-1 (in addition to BCrypt)

Add a password hash provider to handle BCrypt passwords inside Keycloak.
Add a password hash provider to handle SHA-1 passwords inside Keycloak.

## Build JAR

```bash
./gradlew assemble -Pdependency.keycloak.version=${KEYCLOAK_VERSION}
```

## Build Docker image

```bash
docker build \
    --build-arg keycloak_version=${KEYCLOAK_VERSION} \
    -t ghcr.io/jcschaff/keycloak-sha1 \
    .
```

## Test with docker-compose

```bash
docker-compose up -d
```

## Install

### >= 17.0.0

```bash
curl -L https://github.com/jcschaff/keycloak-sha1/releases/download/${KEYCLOAK_SHA1_VERSION}/keycloak-sha1-${KEYCLOAK_SHA1_VERSION}.jar > ${KEYCLOAK_HOME}/providers/keycloak-sha1-${KEYCLOAK_SHA1_VERSION}.jar
```
You need to restart Keycloak.

### < 17.0.0

```bash
curl -L https://github.com/jcschaff/keycloak-sha1/releases/download/${KEYCLOAK_SHA1_VERSION}/keycloak-sha1-${KEYCLOAK_SHA1_VERSION}.jar > ${KEYCLOAK_HOME}/standalone/deployments/keycloak-sha1-${KEYCLOAK_SHA1
_VERSION}.jar
```
You need to restart Keycloak.

## Run with Docker

```bash
docker run \
    -e KEYCLOAK_ADMIN=${KEYCLOAK_ADMIN} \
    -e KEYCLOAK_ADMIN_PASSWORD=${KEYCLOAK_ADMIN_PASSWORD} \
    -e KC_HOSTNAME=${KC_HOSTNAME} \
    ghcr.io/jcschaff/keycloak-sha1 \
    start
```

The image is based on [Keycloak official](https://quay.io/repository/keycloak/keycloak) one.

## How to use
Go to `Authentication` / `Policies` / `Password policy` and add hashing algorithm policy with value `bcrypt`.
Go to `Authentication` / `Policies` / `Password policy` and add hashing algorithm policy with value `sha1`.

To test if installation works, create new user and set its credentials.
