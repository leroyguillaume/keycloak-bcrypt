package com.github.leroyguillaume.keycloak.bcrypt;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.LongPasswordStrategies;
import at.favre.lib.crypto.bcrypt.LongPasswordStrategy;
import org.keycloak.credential.hash.PasswordHashProvider;
import org.keycloak.models.PasswordPolicy;
import org.keycloak.models.credential.PasswordCredentialModel;

import java.security.SecureRandom;


/**
 * @author <a href="mailto:pro.guillaume.leroy@gmail.com">Guillaume Leroy</a>
 */
public class BCryptPasswordHashProvider implements PasswordHashProvider {
    private static final BCrypt.Version VERSION = BCrypt.Version.VERSION_2A;
    // bcrypt only reads the first 72 bytes; truncating instead of throwing matches OpenBSD, PHP and htpasswd,
    // so long passwords hashed elsewhere still verify.
    private static final LongPasswordStrategy LONG_PASSWORD_STRATEGY = LongPasswordStrategies.truncate(VERSION);

    private final int defaultIterations;
    private final String providerId;
    private final BCrypt.Hasher hasher = BCrypt.with(VERSION, new SecureRandom(), LONG_PASSWORD_STRATEGY);
    private final BCrypt.Verifyer verifyer = BCrypt.verifyer(VERSION, LONG_PASSWORD_STRATEGY);

    public BCryptPasswordHashProvider(final String providerId, final int defaultIterations) {
        this.providerId = providerId;
        this.defaultIterations = defaultIterations;
    }

    @Override
    public boolean policyCheck(final PasswordPolicy policy, final PasswordCredentialModel credential) {
        return policyCheck(policy.getHashIterations(), credential);
    }

    boolean policyCheck(final int policyIterations, final PasswordCredentialModel credential) {
        final int storedIterations = credential.getPasswordCredentialData().getHashIterations();
        return cost(storedIterations) == cost(policyIterations)
                && providerId.equals(credential.getPasswordCredentialData().getAlgorithm());
    }

    @Override
    public PasswordCredentialModel encodedCredential(final String rawPassword, final int iterations) {
        final int cost = cost(iterations);
        final String encodedPassword = encode(rawPassword, cost);

        // bcrypt salt is stored as part of the encoded password so no need to store salt separately
        return PasswordCredentialModel.createFromValues(providerId, new byte[0], cost, encodedPassword);
    }

    @Override
    public String encode(final String rawPassword, final int iterations) {
        return hasher.hashToString(cost(iterations), rawPassword.toCharArray());
    }

    @Override
    public void close() {

    }

    @Override
    public boolean verify(final String rawPassword, final PasswordCredentialModel credential) {
        final String hash = credential.getPasswordSecretData().getValue();

        if (hash == null || hash.isEmpty()) {
            return false;
        }

        return verifyer.verify(rawPassword.toCharArray(), hash.toCharArray()).verified;
    }

    private int cost(final int iterations) {
        return iterations == -1 ? defaultIterations : iterations;
    }
}
