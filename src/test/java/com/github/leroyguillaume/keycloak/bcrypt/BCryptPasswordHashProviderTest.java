package com.github.leroyguillaume.keycloak.bcrypt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.keycloak.models.credential.PasswordCredentialModel;

import static org.junit.jupiter.api.Assertions.*;

class BCryptPasswordHashProviderTest {
    private final int iterations = 10;
    private final String id = "bcrypt";
    private final BCryptPasswordHashProvider provider = new BCryptPasswordHashProvider(id, iterations);

    @Test
    @DisplayName("Should hash the password successfully")
    void shouldHashThePasswordSuccessfully() {
        String rawPassword = "test";
        String hashedPassword = provider.encode(rawPassword, iterations);
        PasswordCredentialModel model = PasswordCredentialModel.createFromValues(id, new byte[0], iterations, hashedPassword);

        assertNotNull(hashedPassword);
        assertTrue(provider.verify(rawPassword, model));
    }

    @Test
    @DisplayName("Should return false when hash is null")
    void shouldReturnFalseWhenHashIsNull() {
        String rawPassword = "test";
        PasswordCredentialModel model = PasswordCredentialModel.createFromValues(id, new byte[0], iterations, null);

        assertFalse(provider.verify(rawPassword, model));
    }

    @Test
    @DisplayName("Should return false when hash is empty")
    void shouldReturnFalseWhenHashIsEmpty() {
        String rawPassword = "test";
        PasswordCredentialModel model = PasswordCredentialModel.createFromValues(id, new byte[0], iterations, "");

        assertFalse(provider.verify(rawPassword, model));
    }
}
