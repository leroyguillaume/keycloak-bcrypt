plugins {
    java
}

group = "com.github.leroyguillaume"
version = "1.4.0"

repositories {
    mavenCentral()
}

dependencies {
    val jbcryptVersion = "0.4"
    val jbossLoggingVersion = "3.4.1.Final"
    val keycloakVersion = "10.0.1"

    // JBcrypt
    implementation("org.mindrot:jbcrypt:$jbcryptVersion")

    // JBoss
    implementation("org.jboss.logging:jboss-logging:$jbossLoggingVersion")

    // Keycloak
    implementation("org.keycloak:keycloak-common:$keycloakVersion")
    implementation("org.keycloak:keycloak-core:$keycloakVersion")
    implementation("org.keycloak:keycloak-server-spi:$keycloakVersion")
    implementation("org.keycloak:keycloak-server-spi-private:$keycloakVersion")
}

tasks {
    wrapper {
        gradleVersion = "6.4"
    }
}
