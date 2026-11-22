package com.github.leroyguillaume.keycloak.bcrypt;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.keycloak.models.PasswordPolicy;
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

    @Test
    @DisplayName("Should return false when password is wrong")
    void shouldReturnFalseWhenPasswordIsWrong() {
        PasswordCredentialModel model = provider.encodedCredential("test", iterations);

        assertFalse(provider.verify("nope", model));
    }

    @Test
    @DisplayName("Should store the default cost when iterations are unset")
    void shouldStoreTheDefaultCostWhenIterationsAreUnset() {
        PasswordCredentialModel model = provider.encodedCredential("test", -1);

        assertEquals(iterations, model.getPasswordCredentialData().getHashIterations());
        assertTrue(model.getPasswordSecretData().getValue().startsWith("$2a$10$"));
    }

    @Test
    @DisplayName("Should not require a rehash under the default policy")
    void shouldNotRequireARehashUnderTheDefaultPolicy() {
        PasswordCredentialModel model = provider.encodedCredential("test", -1);

        assertTrue(provider.policyCheck(PasswordPolicy.empty(), model));
    }

    @Test
    @DisplayName("Should not require a rehash for credentials stored with unset iterations")
    void shouldNotRequireARehashForCredentialsStoredWithUnsetIterations() {
        PasswordCredentialModel model = PasswordCredentialModel.createFromValues(id, new byte[0], -1, provider.encode("test", -1));

        assertTrue(provider.policyCheck(PasswordPolicy.empty(), model));
        assertTrue(provider.policyCheck(iterations, model));
    }

    @Test
    @DisplayName("Should require a rehash when the policy cost differs")
    void shouldRequireARehashWhenThePolicyCostDiffers() {
        PasswordCredentialModel model = provider.encodedCredential("test", iterations);

        assertFalse(provider.policyCheck(12, model));
    }

    @Test
    @DisplayName("Should require a rehash when the algorithm differs")
    void shouldRequireARehashWhenTheAlgorithmDiffers() {
        PasswordCredentialModel model = PasswordCredentialModel.createFromValues("pbkdf2-sha256", new byte[0], iterations, "hash");

        assertFalse(provider.policyCheck(iterations, model));
    }

    @Test
    @DisplayName("Should hash and verify passwords longer than 72 bytes")
    void shouldHashAndVerifyPasswordsLongerThan72Bytes() {
        String rawPassword = "x".repeat(100);
        PasswordCredentialModel model = provider.encodedCredential(rawPassword, iterations);

        assertTrue(provider.verify(rawPassword, model));
        assertTrue(provider.verify("x".repeat(72), model));
        assertFalse(provider.verify("x".repeat(71), model));
    }

    @Test
    @DisplayName("Should verify a long password hashed by htpasswd")
    void shouldVerifyALongPasswordHashedByHtpasswd() {
        // htpasswd -nbB -C 4 u "$(printf 'x%.0s' {1..100})"
        String hash = "$2y$04$S7V4kHC8r6CAvFxSLU/ATOZ8yaYHAYYUId2uK/qgluPaeUT6LFtla";
        PasswordCredentialModel model = PasswordCredentialModel.createFromValues(id, new byte[0], 4, hash);

        assertTrue(provider.verify("x".repeat(100), model));
    }

    @Test
    @DisplayName("Should verify a 2b hash")
    void shouldVerifyA2bHash() {
        String hash = BCrypt.with(BCrypt.Version.VERSION_2B).hashToString(4, "test".toCharArray());
        PasswordCredentialModel model = PasswordCredentialModel.createFromValues(id, new byte[0], 4, hash);

        assertTrue(provider.verify("test", model));
    }
}
