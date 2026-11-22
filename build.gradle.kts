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

dependencyLocking {
    lockAllConfigurations()
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

    testRuntimeOnly(libs.jboss.logging)
}

testing {
    suites {
        named<JvmTestSuite>("test") {
            useJUnitJupiter(libs.versions.junit)
        }

        register<JvmTestSuite>("integrationTest") {
            useJUnitJupiter(libs.versions.junit)

            dependencies {
                implementation(libs.testcontainers.keycloak)
                runtimeOnly(libs.slf4j.simple)
            }

            targets.all {
                testTask.configure {
                    description = "Runs the provider inside Keycloak containers (needs Docker)."
                    shouldRunAfter(tasks.test)

                    val providerJar = tasks.shadowJar.flatMap { it.archiveFile }
                    val keycloakImage = providers.gradleProperty("keycloakImage")
                    inputs.file(providerJar)
                    inputs.property("keycloakImage", keycloakImage)
                    jvmArgumentProviders.add {
                        listOf(
                            "-Dprovider.jar=${providerJar.get().asFile.absolutePath}",
                            "-Dkeycloak.image=${keycloakImage.get()}",
                        )
                    }
                }
            }
        }
    }
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

    wrapper {
        gradleVersion = "9.9.2"
    }
}
