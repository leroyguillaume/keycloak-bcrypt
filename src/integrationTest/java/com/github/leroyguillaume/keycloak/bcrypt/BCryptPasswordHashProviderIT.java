package com.github.leroyguillaume.keycloak.bcrypt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import jakarta.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

class BCryptPasswordHashProviderIT {
    // htpasswd -nbB -C 10 u password
    private static final String HTPASSWD_HASH = "$2y$10$iQR7dy/xpBXt.dlkn.HcSuDYVixpeF.nmRT.QNiGkrog8vXVp2jAK";

    private static final HttpClient HTTP = HttpClient.newHttpClient();
    private static final ObjectMapper JSON = new ObjectMapper();

    private static KeycloakContainer keycloak;
    private static Keycloak admin;

    @BeforeAll
    static void startKeycloak() {
        String image = System.getProperty("keycloak.image");
        keycloak = new KeycloakContainer(image)
                .withProviderLibsFrom(List.of(new File(System.getProperty("provider.jar"))))
                .withCustomCommand(serverDefaultOption(image));
        keycloak.start();
        admin = keycloak.getKeycloakAdminClient();
    }

    @AfterAll
    static void stopKeycloak() {
        admin.close();
        keycloak.stop();
    }

    @Test
    @DisplayName("Should hash with bcrypt when it is the server default")
    void shouldHashWithBcryptWhenItIsTheServerDefault() throws Exception {
        String realm = createRealm(null);
        UserResource user = createUser(realm, passwordCredential("s3cret"));

        JsonNode data = credentialData(user);
        assertEquals("bcrypt", data.get("algorithm").asText());
        assertEquals(10, data.get("hashIterations").asInt());
        assertAccepted(realm, "s3cret");
        assertRejected(realm, "wrong");
    }

    @Test
    @DisplayName("Should hash with the realm policy cost")
    void shouldHashWithTheRealmPolicyCost() throws Exception {
        String realm = createRealm("hashAlgorithm(bcrypt) and hashIterations(12)");
        UserResource user = createUser(realm, passwordCredential("s3cret"));

        assertEquals(12, credentialData(user).get("hashIterations").asInt());
        assertAccepted(realm, "s3cret");
    }

    @Test
    @DisplayName("Should verify an imported htpasswd hash")
    void shouldVerifyAnImportedHtpasswdHash() throws Exception {
        String realm = createRealm("hashAlgorithm(bcrypt)");
        createUser(realm, importedCredential(HTPASSWD_HASH, 10));

        assertAccepted(realm, "password");
        assertRejected(realm, "wrong");
    }

    @Test
    @DisplayName("Should rehash on login when the policy cost changed")
    void shouldRehashOnLoginWhenThePolicyCostChanged() throws Exception {
        String realm = createRealm("hashAlgorithm(bcrypt) and hashIterations(11)");
        UserResource user = createUser(realm, importedCredential(HTPASSWD_HASH, 10));

        assertAccepted(realm, "password");
        assertEquals(11, credentialData(user).get("hashIterations").asInt());
        assertAccepted(realm, "password");
    }

    @Test
    @DisplayName("Should accept passwords longer than 72 bytes")
    void shouldAcceptPasswordsLongerThan72Bytes() throws Exception {
        String realm = createRealm("hashAlgorithm(bcrypt)");
        String password = "x".repeat(100);
        createUser(realm, passwordCredential(password));

        assertAccepted(realm, password);
    }

    // Keycloak < 26.3 silently ignores the `--spi-<spi>--<option>` format, so the README documents both.
    private static String serverDefaultOption(final String image) {
        Runtime.Version version = Runtime.Version.parse(image.substring(image.lastIndexOf(':') + 1));
        return version.compareTo(Runtime.Version.parse("26.3")) < 0
                ? "--spi-password-hashing-provider-default=bcrypt"
                : "--spi-password-hashing--provider-default=bcrypt";
    }

    private static String createRealm(final String passwordPolicy) {
        RealmRepresentation realm = new RealmRepresentation();
        realm.setRealm("test-" + UUID.randomUUID());
        realm.setEnabled(true);
        realm.setPasswordPolicy(passwordPolicy);
        admin.realms().create(realm);
        return realm.getRealm();
    }

    private static UserResource createUser(final String realm, final CredentialRepresentation credential) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername("alice");
        user.setEmail("alice@example.com");
        user.setFirstName("Alice");
        user.setLastName("Liddell");
        user.setEnabled(true);
        user.setCredentials(List.of(credential));
        try (Response response = admin.realm(realm).users().create(user)) {
            assertEquals(201, response.getStatus());
            String path = response.getLocation().getPath();
            return admin.realm(realm).users().get(path.substring(path.lastIndexOf('/') + 1));
        }
    }

    private static CredentialRepresentation passwordCredential(final String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        return credential;
    }

    private static CredentialRepresentation importedCredential(final String hash, final int cost) throws IOException {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setSecretData(JSON.writeValueAsString(Map.of("value", hash)));
        credential.setCredentialData(JSON.writeValueAsString(Map.of("hashIterations", cost, "algorithm", "bcrypt")));
        return credential;
    }

    private static JsonNode credentialData(final UserResource user) throws IOException {
        List<CredentialRepresentation> credentials = user.credentials();
        assertEquals(1, credentials.size());
        return JSON.readTree(credentials.get(0).getCredentialData());
    }

    private static void assertAccepted(final String realm, final String password)
            throws IOException, InterruptedException {
        HttpResponse<String> response = login(realm, password);
        assertEquals(200, response.statusCode(), response.body());
    }

    private static void assertRejected(final String realm, final String password)
            throws IOException, InterruptedException {
        JsonNode error = JSON.readTree(login(realm, password).body());
        assertEquals("invalid_grant", error.get("error").asText());
        assertEquals("Invalid user credentials", error.get("error_description").asText());
    }

    private static HttpResponse<String> login(final String realm, final String password)
            throws IOException, InterruptedException {
        String form = "grant_type=password&client_id=admin-cli&username=alice&password="
                + URLEncoder.encode(password, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(keycloak.getAuthServerUrl() + "/realms/" + realm + "/protocol/openid-connect/token"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();
        return HTTP.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
