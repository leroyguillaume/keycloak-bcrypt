package com.github.leroyguillaume.keycloak.bcrypt;

import org.jboss.logging.Logger;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.keycloak.credential.CredentialModel;
import org.keycloak.credential.hash.PasswordHashProvider;
import org.keycloak.models.PasswordPolicy;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.models.credential.dto.PasswordCredentialData;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

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
    public boolean policyCheck(PasswordPolicy passwordPolicy, PasswordCredentialModel passwordCredentialModel) {
        int policyHashIterations = passwordPolicy.getHashIterations();
        if (policyHashIterations == -1) {
            policyHashIterations = defaultIterations;
        }

        PasswordCredentialData credential = passwordCredentialModel.getPasswordCredentialData();
        return credential.getHashIterations() == policyHashIterations && providerId.equals(credential.getAlgorithm());
    }

    @Override
    public String encode(String rawPassword, int iterations) {
        int logRounds = iterations == -1 ? iterationsToLogRounds(defaultIterations) : iterationsToLogRounds(iterations);
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(logRounds));
    }

    @Override
    public PasswordCredentialModel encodedCredential(String rawPassword, int iterations) {
        return PasswordCredentialModel.createFromValues(
                providerId,
                // Salt encoding is modified base64 so standard decode does not work
                // No need to actually record salt separately
                new byte[0],
                iterations == -1 ? defaultIterations : iterations,
                this.encode(rawPassword, iterations)
        );
    }

    @Override
    public void close() {

    }

    @Override
    public boolean verify(String rawPassword, PasswordCredentialModel passwordCredentialModel) {
        return BCrypt.checkpw(rawPassword, passwordCredentialModel.getPasswordSecretData().getValue());
    }

    private int iterationsToLogRounds(int iterations) {
         // bcrypt uses 2**log2_rounds with a min of 4 and max of 30 log rounds
        return Math.max(MIN_BCRYPT_LOG_ROUNDS, Math.min(MAX_BCRYPT_LOG_ROUNDS,
                (int) Math.round(Math.log(iterations) / Math.log(2) + 1)));
    }
}
