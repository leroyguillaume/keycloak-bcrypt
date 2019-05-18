# Keycloak BCrypt

Add a password hash provider to handle BCrypt passwords inside Keycloak.

## Installation steps

### Build

1. Clone repository
2. Run `mvn clean package`

### Install jbcrypt dependancy

1. Download jbcrypt dependancy
```
$ curl http://central.maven.org/maven2/org/mindrot/jbcrypt/0.4/jbcrypt-0.4.jar > jbcrypt-0.4.jar
```

2. Add as a module using jboss cli tool
```
$ KEYCLOAK_HOME/bin/jboss-cli.sh \
    --command="module add \
        --name=org.mindrot.jbcrypt \
        --resources="jbcrypt-0.4.jar"
```

### Copy keyload-bcrypt to deployments folder
```
cp target/*.jar KEYCLOAK_HOME/standalone/deployments
```

You need to restart Keycloak.
