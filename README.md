# Keycloak BCrypt

Add a password hash provider to handle BCrypt passwords inside Keycloak.

## Build
```
mvn clean package
```

## Install
```
curl https://repo1.maven.org/maven2/org/mindrot/jbcrypt/0.4/jbcrypt-0.4.jar > jbcrypt-0.4.jar
KEYCLOAK_HOME/bin/jboss-cli.sh --command="module add --name=org.mindrot.jbcrypt --resources=jbcrypt-0.4.jar"
curl -L https://github.com/leroyguillaume/keycloak-bcrypt/releases/download/1.2.0/keycloak-bcrypt-1.2.0.jar > KEYCLOAK_HOME/standalone/deployments/keycloak-bcrypt-1.2.0.jar
```
You need to restart Keycloak.

## Install with Docker Compose with volume

`mkdir -p keycloak/bcrypt/dependency/jbcrypt`

Add the following `module.xml` file in directory created above:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.5" name="org.mindrot.jbcrypt">
    <resources>
        <resource-root path="jbcrypt-0.4.jar"/>
    </resources>
</module>
```

`curl https://repo1.maven.org/maven2/org/mindrot/jbcrypt/0.4/jbcrypt-0.4.jar > ./keycloak/bcrypt/dependency/jbcryptjbcrypt-0.4.jar`


`mkdir -p keycloak/bcrypt/deployments`

`curl -L https://github.com/leroyguillaume/keycloak-bcrypt/releases/download/1.2.0/keycloak-bcrypt-1.2.0.jar > ./keycloak/bcrypt/deployments`


docker-compose.yml
```yml
keycloak:
    image: jboss/keycloak:9.0.3
    volumes:
      # Adds bcrypt support for password encoding
      - ./keycloak/bcrypt/dependency/jbcrypt:/opt/jboss/keycloak/modules/org/mindrot/jbcrypt/main
      - ./keycloak/bcrypt/deployments:/opt/jboss/keycloak/standalone/deployments
```

## Install with Docker

```bash
docker build .
```

Next add the following Docker Compose service:

```yml
  keycloak:
    build:
      context: .
      dockerfile: Dockerfile
```
