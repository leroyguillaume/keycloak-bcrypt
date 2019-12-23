package com.github.leroyguillaume.keycloak.bcrypt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.models.PasswordPolicy;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

class BCryptPasswordHashProviderTest {
    private static int DEFAULT_ITERATIONS = 15;
    private BCryptPasswordHashProvider hashProvider;

    @BeforeEach
    void setHashProvider() {
        this.hashProvider = new BCryptPasswordHashProvider("test-bcrypt", DEFAULT_ITERATIONS);
    }

    @Test
    void policyCheckEmpty() {
        PasswordPolicy passwordPolicy = PasswordPolicy.empty();
        PasswordCredentialModel credentialModel = PasswordCredentialModel.createFromValues(
                "", new byte[0], 1, "");
        assertFalse(this.hashProvider.policyCheck(passwordPolicy, credentialModel));
    }

    @Test
    void encode() {
        String encodedPassword = this.hashProvider.encode("test", -1);
        String[] passwordParts = encodedPassword.split("\\$");
        int logRounds = Integer.parseInt(passwordParts[2]);
        assertEquals(5, logRounds);

        encodedPassword = this.hashProvider.encode("test", 10);
        passwordParts = encodedPassword.split("\\$");
        logRounds = Integer.parseInt(passwordParts[2]);
        assertEquals(4, logRounds);
    }

    @Test
    void encodedCredential() {
        PasswordCredentialModel credentialModel = this.hashProvider.encodedCredential("test", -1);
        assertEquals("test-bcrypt", credentialModel.getPasswordCredentialData().getAlgorithm());
        assertEquals(DEFAULT_ITERATIONS, credentialModel.getPasswordCredentialData().getHashIterations());
        assertTrue(BCrypt.checkpw("test", credentialModel.getPasswordSecretData().getValue()));
        assertArrayEquals(new byte[0], credentialModel.getPasswordSecretData().getSalt());

        credentialModel = this.hashProvider.encodedCredential("test", 10);
        assertEquals(10, credentialModel.getPasswordCredentialData().getHashIterations());
    }

    @Test
    void verify() {
        PasswordCredentialModel credentialModel = this.hashProvider.encodedCredential("test", -1);
        this.hashProvider.verify("test", credentialModel);
    }
}
