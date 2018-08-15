package com.github.leroyguillaume.keycloak.bcrypt;

import org.jboss.logging.Logger;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.keycloak.credential.CredentialModel;
import org.keycloak.credential.hash.PasswordHashProvider;
import org.keycloak.models.PasswordPolicy;
import org.keycloak.models.UserCredentialModel;
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

    public BCryptPasswordHashProvider(String providerId, int defaultIterations) {
        this.providerId = providerId;
        this.defaultIterations = defaultIterations;
    }

    @Override
    public boolean policyCheck(PasswordPolicy policy, CredentialModel credential) {
        int policyHashIterations = policy.getHashIterations();
        if (policyHashIterations == -1) {
            policyHashIterations = defaultIterations;
        }

        return credential.getHashIterations() == policyHashIterations && providerId.equals(credential.getAlgorithm());
    }

    @Override
    public String encode(String rawPassword, int iterations) {
        int logRounds = iterations == -1 ? iterationsToLogRounds(defaultIterations) : iterationsToLogRounds(iterations);
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(logRounds));
    }

    @Override
    public void encode(String rawPassword, int iterations, CredentialModel credential) {
        if (iterations == -1) {
            iterations = defaultIterations;
        }

        String salt = BCrypt.gensalt(iterationsToLogRounds(iterations));
        String password = BCrypt.hashpw(rawPassword, salt);

        credential.setAlgorithm(providerId);
        credential.setType(UserCredentialModel.PASSWORD);
        credential.setHashIterations(iterations);
        credential.setValue(password);

        // Salt encoding is modified base64 so standard decode does not work
        // No need to actually record salt separately
        credential.setSalt(new byte[0]);
    }

    @Override
    public void close() {

    }

    @Override
    public boolean verify(String rawPassword, CredentialModel credential) {
        return BCrypt.checkpw(rawPassword, credential.getValue());
    }

    private int iterationsToLogRounds(int iterations) {
         // bcrypt uses 2**log2_rounds with a min of 4 and max of 30 log rounds
        return Math.max(MIN_BCRYPT_LOG_ROUNDS, Math.min(MAX_BCRYPT_LOG_ROUNDS,
                (int) Math.round(Math.log(iterations) / Math.log(2) + 1)));
    }
}
