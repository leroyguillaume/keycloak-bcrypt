# Keycloak BCrypt

Add a password hash provider to handle BCrypt passwords inside Keycloak.

## Build
```bash
./gradlew jar
```

## Test with docker-compose
```bash
cp build/libs/keycloak-bcrypt-1.5.0.jar docker/
docker-compose up -d
# Waiting for Keycloak startup
docker-compose exec keycloak /opt/jboss/keycloak/bin/add-user-keycloak.sh -u admin -p admin # to create admin user
docker-compose restart keycloak
```

## Install
```
curl -L https://github.com/leroyguillaume/keycloak-bcrypt/releases/download/1.5.0/keycloak-bcrypt-1.5.0.jar > KEYCLOAK_HOME/standalone/deployments/keycloak-bcrypt-1.5.0.jar
```
You need to restart Keycloak.

## How to use
Go to `Authentication` / `Password policy` and add hashing algorithm policy with value `bcrypt`.

To test if installation works, create new user and set its credentials.
