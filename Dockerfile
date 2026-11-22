ARG KEYCLOAK_IMAGE=quay.io/keycloak/keycloak:26.8.2@sha256:7e66a96e4739239070aeaf72e4c5decf0f7f682cfc725b9f7ea2b336eaba38b3

FROM ${KEYCLOAK_IMAGE}

# The jar is built outside the image, so the image ships the same bytes as the attested release jar.
COPY build/libs/keycloak-bcrypt-*.jar /opt/keycloak/providers/

ENV KC_HEALTH_ENABLED=true

HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD ["bash", "-c", "exec 3<>/dev/tcp/127.0.0.1/9000 && printf 'GET /health/ready HTTP/1.0\\r\\n\\r\\n' >&3 && grep -q ' 200 ' <&3"]

# Not the usual dedicated UID 65532: the upstream image's /opt/keycloak belongs to its keycloak user (UID 1000),
# and Keycloak writes its build there at startup.
USER 1000
