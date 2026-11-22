# Security policy

## Reporting a vulnerability

Report it privately through [GitHub's private vulnerability reporting](https://github.com/leroyguillaume/keycloak-bcrypt/security/advisories/new). Never in a public issue, pull request or discussion: a vulnerability described in public is disclosed before it is fixed.

A useful report holds:

- the provider version, and the Keycloak version it runs on;
- the steps to reproduce;
- the impact as you see it.

## Supported versions

Fixes ship in a new release; only the latest release is supported.

## Scope

In scope:

- the provider's code: a password that verifies against the wrong hash, a hash weaker than its configured cost, a crash a login request can trigger;
- the jar and the Docker image this repository publishes.

Out of scope:

- a vulnerability in Keycloak itself: report it to [Keycloak](https://github.com/keycloak/keycloak/security/policy);
- a vulnerability in a dependency that is already public upstream: report it there, the fix comes in with the dependency update;
- bcrypt's 72-byte password limit, which is part of the algorithm;
- a finding that needs an already-compromised host or admin account;
- scanner output with no path to exploitation.

## What happens next

Reports are handled on a best-effort basis.

1. The report is acknowledged and assessed in its draft advisory.
2. The fix is prepared in the advisory's private fork.
3. A release ships the fix.
4. The advisory is published, with a CVE requested through GitHub when the impact warrants one, and the reporter credited unless they prefer otherwise.
