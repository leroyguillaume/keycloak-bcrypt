# Keycloak BCrypt

Add a password hash provider to handle BCrypt passwords inside Keycloak.

The password provider supports all current versions of bcrypt. For details please refer to [https://en.wikipedia.org/wiki/Bcrypt].

## Build
```
mvn clean package
```

## Install
```
curl -L https://github.com/leroyguillaume/keycloak-bcrypt/releases/download/1.3.0/keycloak-bcrypt-1.3.0.jar > KEYCLOAK_HOME/standalone/deployments/keycloak-bcrypt-1.3.0.jar
```
You need to restart Keycloak.

## Install with Docker Compose

`mkdir -p ./keycloak/bcrypt/dependency/jbcrypt`

Add the following `module.xml` file in directory created above:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.5" name="at.favre.lib.jbcrypt">
    <resources>
        <resource-root path="bcrypt-0.9.0.jar"/>
    </resources>
</module>
```

`https://repo1.maven.org/maven2/at/favre/lib/bcrypt/0.9.0/bcrypt-0.9.0.jar > ./keycloak/bcrypt/dependency/bcrypt-0.9.0.jar`


`mkdir -p ./keycloak/bcrypt/deployments`

`curl -L https://github.com/leroyguillaume/keycloak-bcrypt/releases/download/1.3.0/keycloak-bcrypt-1.3.0.jar > ./keycloak/bcrypt/deployments`


docker-compose.yml
```yml
keycloak:
    image: jboss/keycloak:9.0.0
    volumes:
      # Adds bcrypt support for password encoding
      - ./keycloak/bcrypt/dependency/jbcrypt:/opt/jboss/keycloak/modules/org/mindrot/jbcrypt/main
      - ./keycloak/bcrypt/deployments:/opt/jboss/keycloak/standalone/deployments
```

## Porting WordPress users to KeyCloak
This module can be used to port an existing user database from WordPress internal database to KeyCloak without the need for new user passwords.
The WordPress passwords can be inserted directly into the CREDENTIALS table in KeyCloak. Below is an example of the two columns in the database:

| SECRET_DATA | CREDENTIAL_DATA |
| :-- | :-- |
| {"value":"$2y$10$Ma/RzN/J089o4gCs1MbzcOTvkbGXvmkEJXwNh3a3Bj1ZTnlwi93u.","salt":""} | {"hashIterations":-1,"algorithm":"bcrypt"}|

Please note that the hashIterations must either be -1 or match the defualt set up in keycloak.