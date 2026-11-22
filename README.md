# Keycloak BCrypt

[![OpenSSF Scorecard](https://api.scorecard.dev/projects/github.com/leroyguillaume/keycloak-bcrypt/badge)](https://scorecard.dev/viewer/?uri=github.com/leroyguillaume/keycloak-bcrypt)

A Keycloak password hash provider that stores and verifies passwords with bcrypt.

## Description

Keycloak hashes passwords with PBKDF2 or Argon2. This provider adds a `bcrypt` hashing algorithm, so users coming from an application that stored bcrypt hashes — PHP's `password_hash`, Spring Security, htpasswd, Devise — keep their passwords: import the hashes as they are, and they verify on the next login.

- Verifies `$2a$`, `$2b$` and `$2y$` hashes, and stores new ones as `$2a$`.
- Only the first 72 bytes of a password count, as in every bcrypt implementation: longer passwords are truncated, the way OpenBSD and PHP do it. A *Maximum length* password policy makes the limit visible to users.
- The cost (work factor) defaults to 10 and follows the realm's *Hashing Iterations* policy.

It does not migrate users by itself: importing them goes through the Keycloak admin API, as shown below.

## Getting started

### Prerequisites

- Keycloak ≥ 26.0

### Installation

Download the provider into Keycloak's `providers` directory:

```bash
curl -L -o /opt/keycloak/providers/keycloak-bcrypt-1.7.0.jar https://github.com/leroyguillaume/keycloak-bcrypt/releases/download/v1.7.0/keycloak-bcrypt-1.7.0.jar
```

Check that this repository's release workflow built it:

```bash
gh attestation verify /opt/keycloak/providers/keycloak-bcrypt-1.7.0.jar --repo leroyguillaume/keycloak-bcrypt
```

Then restart Keycloak. A server started with `--optimized` needs a build first:

```bash
/opt/keycloak/bin/kc.sh build
```

Or use the image: the [official Keycloak image](https://quay.io/repository/keycloak/keycloak) with the provider added, for `amd64` and `arm64`, on Docker Hub (`gleroy/keycloak-bcrypt`) and GHCR (`ghcr.io/leroyguillaume/keycloak-bcrypt`). It is tagged `<keycloak version>_<provider version>`. Each new Keycloak release gets an image of the latest provider release, and `latest` is the newest image:

```bash
docker run -p 8080:8080 -e KC_BOOTSTRAP_ADMIN_USERNAME=admin -e KC_BOOTSTRAP_ADMIN_PASSWORD=change-me gleroy/keycloak-bcrypt:26.5.2_1.7.0 start-dev
```

```bash
gh attestation verify oci://docker.io/gleroy/keycloak-bcrypt:26.5.2_1.7.0 --repo leroyguillaume/keycloak-bcrypt
```

The image enables Keycloak's health endpoints on the management port, 9000, which its `HEALTHCHECK` probes.

### Configuration

Make bcrypt a realm's hashing algorithm, in *Authentication* › *Policies* › *Password policy* › *Hashing Algorithm* set to `bcrypt`, or with `kcadm.sh`:

```bash
kcadm.sh update realms/<realm> -s 'passwordPolicy=hashAlgorithm(bcrypt)'
```

Or make it the default of every realm without a hashing policy, with a server option:

```bash
kc.sh start --spi-password-hashing--provider-default=bcrypt
```

Keycloak 26.0 to 26.2 silently ignore that spelling and need the older one:

```bash
kc.sh start --spi-password-hashing-provider-default=bcrypt
```

| Setting | Default | Description |
| --- | --- | --- |
| *Hashing Algorithm* policy | Keycloak's default | `bcrypt` to hash the realm's passwords with this provider |
| *Hashing Iterations* policy | `10` | bcrypt cost, from 4 to 31; each step doubles the hashing time |

A credential whose algorithm or cost differs from the realm's policy is rehashed on the user's next successful login.

### Usage

Users whose password is set after the policy get a bcrypt credential. To import a user with an existing hash, pass it as the credential's `secretData`, with the cost read from the hash (`$2y$10$…` → `10`):

```bash
kcadm.sh create users -r <realm> -f - <<'JSON'
{
  "username": "alice",
  "email": "alice@example.com",
  "firstName": "Alice",
  "lastName": "Liddell",
  "enabled": true,
  "credentials": [
    {
      "type": "password",
      "secretData": "{\"value\":\"$2y$10$iQR7dy/xpBXt.dlkn.HcSuDYVixpeF.nmRT.QNiGkrog8vXVp2jAK\"}",
      "credentialData": "{\"hashIterations\":10,\"algorithm\":\"bcrypt\"}"
    }
  ]
}
JSON
```

Alice then logs in with `password`, the password behind that hash.

To try it locally, build the jar and the image from this repository, and start Keycloak with bcrypt as the default algorithm on <http://localhost:8080> (admin / admin):

```bash
task run
```

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).

## License

Apache-2.0 — see [LICENSE](LICENSE).
