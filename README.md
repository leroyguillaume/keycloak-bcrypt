# Keycloak BCrypt

Add a password hash provider to handle BCrypt passwords inside Keycloak.

## Build
```bash
mvn clean package
```

## Test with docker-compose
```bash
./init-keycloak.sh
```

## Install
```
curl https://repo1.maven.org/maven2/org/mindrot/jbcrypt/0.4/jbcrypt-0.4.jar > jbcrypt-0.4.jar
KEYCLOAK_HOME/bin/jboss-cli.sh --command="module add --name=org.mindrot.jbcrypt --resources=jbcrypt-0.4.jar"
curl -L https://github.com/leroyguillaume/keycloak-bcrypt/releases/download/1.4.0/keycloak-bcrypt-1.4.0.jar > KEYCLOAK_HOME/standalone/deployments/keycloak-bcrypt-1.4.0.jar
```
You need to restart Keycloak.
