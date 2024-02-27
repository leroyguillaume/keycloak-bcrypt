package com.github.leroyguillaume.keycloak.bcrypt;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.LongPasswordStrategies;

import org.keycloak.credential.hash.PasswordHashProvider;
import org.keycloak.models.PasswordPolicy;
import org.keycloak.models.credential.PasswordCredentialModel;


/**
 * @author <a href="mailto:pro.guillaume.leroy@gmail.com">Guillaume Leroy</a>
 */
public class BCryptPasswordHashProvider implements PasswordHashProvider {
    private final int defaultIterations;
    private final String providerId;

    public BCryptPasswordHashProvider(final String providerId, final int defaultIterations) {
        this.providerId = providerId;
        this.defaultIterations = defaultIterations;
    }

    @Override
    public boolean policyCheck(final PasswordPolicy policy, final PasswordCredentialModel credential) {
        final int policyHashIterations = policy.getHashIterations() == -1 ? defaultIterations : policy.getHashIterations();

        return credential.getPasswordCredentialData().getHashIterations() == policyHashIterations
                && providerId.equals(credential.getPasswordCredentialData().getAlgorithm());
    }

    @Override
    public PasswordCredentialModel encodedCredential(final String rawPassword, final int iterations) {
        final String encodedPassword = encode(rawPassword, iterations);

        // bcrypt salt is stored as part of the encoded password so no need to store salt separately
        return PasswordCredentialModel.createFromValues(providerId, new byte[0], iterations, encodedPassword);
    }

    @Override
    public String encode(final String rawPassword, final int iterations) {
        final int cost = iterations == -1 ? defaultIterations : iterations;
        char[] pw = rawPassword.toCharArray();
        if (pw.length > BCrypt.Version.MAX_PW_LENGTH_BYTE - 1) {
            return BCrypt.with(LongPasswordStrategies.hashSha512(BCrypt.Version.VERSION_2Y)).hashToString(cost, pw);
        } else {
            return BCrypt.with(BCrypt.Version.VERSION_2Y).hashToString(cost, rawPassword.toCharArray());
        }
    }

    @Override
    public void close() {

    }

    @Override
    public boolean verify(final String rawPassword, final PasswordCredentialModel credential) {
        final String hash = credential.getPasswordSecretData().getValue();
        char[] pw = rawPassword.toCharArray();
        if (pw.length > BCrypt.Version.MAX_PW_LENGTH_BYTE - 1) {
            final BCrypt.Result verifier = BCrypt.verifyer(BCrypt.Version.VERSION_2Y, LongPasswordStrategies.truncate(BCrypt.Version.VERSION_2Y)).verify(pw, hash);
            return verifier.verified;
        } else {
            final BCrypt.Result verifier = BCrypt.verifyer(BCrypt.Version.VERSION_2Y).verify(pw, hash.toCharArray());
            return verifier.verified;
        }
    }
}
