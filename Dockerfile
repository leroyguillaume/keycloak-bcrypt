ARG gradle_version=jdk21
ARG keycloak_version=26.4.5

FROM gradle:${gradle_version} AS build

WORKDIR /app

COPY . .

RUN gradle assemble

FROM quay.io/keycloak/keycloak:${keycloak_version}

COPY --from=build /app/build/libs/*.jar /opt/keycloak/providers/
