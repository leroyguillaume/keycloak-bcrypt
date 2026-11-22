plugins {
    java
    alias(libs.plugins.shadow)
    alias(libs.plugins.spotless)
}

group = "com.github.leroyguillaume"
version = "1.7.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

configurations {
    testImplementation {
        extendsFrom(compileOnly.get())
    }
}

dependencies {
    implementation(libs.bcrypt)

    compileOnly(libs.keycloak.core)
    compileOnly(libs.keycloak.server.spi)
    compileOnly(libs.keycloak.server.spi.private)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.jboss.logging)
    testRuntimeOnly(libs.junit.platform.launcher)
}

spotless {
    java {
        palantirJavaFormat(
            libs.versions.palantir.java.format
                .get(),
        )
        removeUnusedImports()
    }
    kotlinGradle {
        ktlint()
    }
}

tasks {
    withType<JavaCompile>().configureEach {
        options.compilerArgs.addAll(listOf("-Xlint:all", "-Werror"))
    }

    jar {
        enabled = false
    }

    shadowJar {
        archiveClassifier = ""
        // Keycloak loads every provider jar into one classpath: relocating keeps our bcrypt copy
        // from clashing with another extension that bundles a different version.
        relocate("at.favre.lib", "com.github.leroyguillaume.keycloak.bcrypt.shaded.at.favre.lib")
        mergeServiceFiles()
    }

    assemble {
        dependsOn(shadowJar)
    }

    test {
        useJUnitPlatform()
    }

    wrapper {
        gradleVersion = "9.9.2"
    }
}
