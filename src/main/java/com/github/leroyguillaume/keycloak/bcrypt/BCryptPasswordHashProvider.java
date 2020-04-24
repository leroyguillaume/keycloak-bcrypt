package com.github.leroyguillaume.keycloak.bcrypt;

import org.jboss.logging.Logger;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.credential.hash.PasswordHashProvider;
import org.keycloak.models.PasswordPolicy;
import at.favre.lib.crypto.bcrypt.BCrypt;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author <a href="mailto:pro.guillaume.leroy@gmail.com">Guillaume Leroy</a>
 */
public class BCryptPasswordHashProvider implements PasswordHashProvider {
    // BCrypt uses min of 4 and max of 30 2**log_rounds
    private final int MAX_BCRYPT_LOG_ROUNDS = 30;
    private final int MIN_BCRYPT_LOG_ROUNDS = 4;

    private final int defaultIterations;
    private final String providerId;

    BCryptPasswordHashProvider(String providerId, int defaultIterations) {
        this.providerId = providerId;
        this.defaultIterations = defaultIterations;
    }

    @Override
    public boolean policyCheck(PasswordPolicy policy, PasswordCredentialModel credential) {
        int policyHashIterations = policy.getHashIterations();
        if (policyHashIterations == -1) {
            policyHashIterations = defaultIterations;
        }

        return (credential.getPasswordCredentialData().getHashIterations() == policyHashIterations ||
                credential.getPasswordCredentialData().getHashIterations() == -1)
                        && providerId.equals(credential.getPasswordCredentialData().getAlgorithm());
    }

    @Override
    public PasswordCredentialModel encodedCredential(String rawPassword, int iterations) {
        String encodedPassword = encode(rawPassword, iterations);

        // bcrypt salt is stored as part of the encoded password so no need to store salt separately
        return PasswordCredentialModel.createFromValues(providerId, new byte[0], iterations, encodedPassword);
    }

    @Override
    public String encode(String rawPassword, int iterations) {
        return BCrypt.with(BCrypt.Version.VERSION_2Y).hashToString(iterations, rawPassword.toCharArray());
    }

    @Override
    public void close() {

    }

    @Override
    public boolean verify(String rawPassword, PasswordCredentialModel credential) {
        final String hash = credential.getPasswordSecretData().getValue();
        BCrypt.Result verifier = BCrypt.verifyer().verify(rawPassword.toCharArray(), hash.toCharArray());
        return verifier.verified;
    }
}