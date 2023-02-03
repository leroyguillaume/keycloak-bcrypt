plugins {
    java
}

group = "com.github.leroyguillaume"
version = "1.5.2"

repositories {
    mavenCentral()
}

configurations {
    testImplementation.get().apply {
        extendsFrom(configurations.compileOnly.get())
    }
}

dependencies {
    val bcryptVersion = "0.9.0"
    val jbossLoggingVersion = "3.4.1.Final"
    val keycloakVersion = project.property("dependency.keycloak.version")
    val junitVersion = "5.8.2"

    // BCrypt
    implementation("at.favre.lib:bcrypt:$bcryptVersion")

    // JBoss
    compileOnly("org.jboss.logging:jboss-logging:$jbossLoggingVersion")

    // Keycloak
    compileOnly("org.keycloak:keycloak-common:$keycloakVersion")
    compileOnly("org.keycloak:keycloak-core:$keycloakVersion")
    compileOnly("org.keycloak:keycloak-server-spi:$keycloakVersion")
    compileOnly("org.keycloak:keycloak-server-spi-private:$keycloakVersion")

    // JUnit
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks {
    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    jar {
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }) {
            exclude("META-INF/MANIFEST.MF")
            exclude("META-INF/*.SF")
            exclude("META-INF/*.DSA")
            exclude("META-INF/*.RSA")
        }
    }

    wrapper {
        gradleVersion = "6.4"
    }

    test {
        useJUnitPlatform()
    }
}
