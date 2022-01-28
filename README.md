# Keycloak BCrypt

Add a password hash provider to handle BCrypt passwords inside Keycloak.

## Build JAR

```bash
./gradlew assemble -Pdependency.keycloak.version=${KEYCLOAK_VERSION}
```

## Build Docker image

```bash
cp build/libs/keycloak-bcrypt-${KEYCLOAK_BCRYPT_VERSION}.jar docker
docker build \
    --build-arg keycloak_version=${KEYCLOAK_VERSION} \
    --build-arg keycloak_bcrypt_version=${KEYCLOAK_BCRYPT_VERSION} \
    -t gleroy/keycloak-bcrypt \
    docker
```

## Test with docker-compose

```bash
docker-compose up -d
# Waiting for Keycloak startup
docker-compose exec keycloak /opt/jboss/keycloak/bin/add-user-keycloak.sh -u admin -p admin # to create admin user
docker-compose restart keycloak
```

## Install

```bash
curl -L https://github.com/leroyguillaume/keycloak-bcrypt/releases/download/${KEYCLOAK_BCRYPT_VERSION}/keycloak-bcrypt-${KEYCLOAK_BCRYPT_VERSION}.jar > KEYCLOAK_HOME/standalone/deployments/keycloak-bcrypt-${KEYCLOAK_BCRYPT_VERSION}.jar
```
You need to restart Keycloak.

## Run with Docker

```bash
docker run \
    -e DB_ADDR=${DB_ADDR} \
    -e DB_DATABASE=${DB_DATABASE} \
    -e DB_USER=${DB_USER} \
    -e DB_PASSWORD=${DB_PASSWORD} \
    gleroy/keycloak-bcrypt
```

The image is based on [Keycloak official](https://hub.docker.com/r/jboss/keycloak/) one.

## How to use
Go to `Authentication` / `Password policy` and add hashing algorithm policy with value `bcrypt`.

To test if installation works, create new user and set its credentials.
