package com.github.jcschaff.keycloak.sha1;

import org.keycloak.credential.hash.PasswordHashProvider;
import org.keycloak.models.PasswordPolicy;
import org.keycloak.models.credential.PasswordCredentialModel;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author <a href="mailto:pro.guillaume.leroy@gmail.com">Guillaume Leroy</a>
 * minor changes since then to support SHA1 instead of BCrypt
 */
public class SHA1PasswordHashProvider implements PasswordHashProvider {
    private final String providerId;

    public SHA1PasswordHashProvider(final String providerId) {
        this.providerId = providerId;
    }

    @Override
    public boolean policyCheck(final PasswordPolicy policy, final PasswordCredentialModel credential) {
        return providerId.equals(credential.getPasswordCredentialData().getAlgorithm());
    }

    @Override
    public PasswordCredentialModel encodedCredential(final String rawPassword, final int iterations) {
        final String encodedPassword = encode(rawPassword, iterations);
        return PasswordCredentialModel.createFromValues(providerId, new byte[0], iterations, encodedPassword);
    }

    @Override
    public String encode(final String rawPassword, final int iterations) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(rawPassword.getBytes());

            // convert the digest byte[] to BigInteger
            BigInteger aux = new BigInteger(1, md.digest());

            // convert BigInteger to 40-char lowercase string using leading 0s
            return String.format("%040x", aux);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void close() {
    }

    @Override
    public boolean verify(final String rawPassword, final PasswordCredentialModel credential) {
        String encodedPassword = this.encode(rawPassword, credential.getPasswordCredentialData().getHashIterations());
        String hash = credential.getPasswordSecretData().getValue();
        return encodedPassword.equals(hash);
    }
}
