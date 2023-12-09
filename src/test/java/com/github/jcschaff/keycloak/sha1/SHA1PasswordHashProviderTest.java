package com.github.jcschaff.keycloak.sha1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.keycloak.models.credential.PasswordCredentialModel;

import static org.junit.jupiter.api.Assertions.*;

class SHA1PasswordHashProviderTest {
    private final String id = "bcrypt";
    private final SHA1PasswordHashProvider provider = new SHA1PasswordHashProvider(id);

    @Test
    @DisplayName("Should hash the password successfully")
    void shouldHashThePasswordSuccessfully() {
        String rawPassword = "test";
        int iterations = 1; // value is ignored
        String hashedPassword = provider.encode(rawPassword, iterations);
        PasswordCredentialModel model = PasswordCredentialModel.createFromValues(id, new byte[0], iterations, hashedPassword);

        assertNotNull(hashedPassword);
        assertTrue(provider.verify(rawPassword, model));
    }
}
