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
    @DisplayName("Should hash long password successfully")
    void shouldHashLongPasswordSuccessfully() {
        String rawPassword = "f36e300f46e8b6b12ce76b69ebf82e52d860026b84f21569e16bcf813361e1e248b3f58587b1";
        String hashedPassword = provider.encode(rawPassword, iterations);
        PasswordCredentialModel model = PasswordCredentialModel.createFromValues(id, new byte[0], iterations, hashedPassword);

        assertNotNull(hashedPassword);
        assertTrue(provider.verify(rawPassword, model));
    }

    @Test
    @DisplayName("Should hash password with null terminator successfully")
    void shouldHashPasswordWithNullTerminatorSuccessfully() {
        String rawPassword = "6d606b27bfa404\\0";
        String hashedPassword = provider.encode(rawPassword, iterations);
        PasswordCredentialModel model = PasswordCredentialModel.createFromValues(id, new byte[0], iterations, hashedPassword);

        assertNotNull(hashedPassword);
        assertTrue(provider.verify(rawPassword, model));
    }

    @Test
    @DisplayName("Should hash long password with null terminator successfully")
    void shouldHashLongPasswordWithNullTerminatorSuccessfully() {
        String rawPassword = "3B6QDq5yLph7wZbBBTUHeTqeyLATgQa0EYPt8o+ve3vnVBVz72NtQAdW748/w7yJ68IsqWqxkjLKcqPoI4A8393j9CV4e9Y=\\0";
        String hashedPassword = provider.encode(rawPassword, iterations);
        PasswordCredentialModel model = PasswordCredentialModel.createFromValues(id, new byte[0], iterations, hashedPassword);

        assertNotNull(hashedPassword);
        assertTrue(provider.verify(rawPassword, model));
    }
}
