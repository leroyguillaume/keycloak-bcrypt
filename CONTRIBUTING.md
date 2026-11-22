# Contributing

Questions and bugs go in [GitHub issues](https://github.com/leroyguillaume/keycloak-bcrypt/issues), changes in pull requests. The short version: `mise install`, `task setup`, then `task lint` and `task test` green before you push.

## Development setup

[mise](https://mise.jdx.dev/getting-started.html) installs every tool the project needs — the JDK, Task, pre-commit and the linters its hooks call — at the versions pinned in [`mise.toml`](mise.toml). Gradle comes from the wrapper. Running Keycloak locally also needs Docker.

From a fresh clone:

```bash
mise trust
```

```bash
mise install
```

```bash
task setup
```

`task build` then produces `build/libs/keycloak-bcrypt-<version>.jar`. See [README.md](README.md#getting-started) for installing and configuring it, and `task run` to start Keycloak with it on <http://localhost:8080>.

## Running the tests

The unit tests need nothing but the JDK:

```bash
task test
```

A single class or test:

```bash
task test -- --tests 'BCryptPasswordHashProviderTest.shouldVerifyA2bHash'
```

The integration tests start Keycloak in a container with the provider jar, so they need a running Docker. They run against the image set by `keycloakImage` in [`gradle.properties`](gradle.properties), and any other release with `-PkeycloakImage`:

```bash
task test:integration
```

```bash
task test:integration -- -PkeycloakImage=quay.io/keycloak/keycloak:26.0.8
```

Dependency versions live in [`gradle/libs.versions.toml`](gradle/libs.versions.toml), and the ones the jar ships are locked in [`gradle.lockfile`](gradle.lockfile), which the `security` workflow scans. After changing a runtime dependency, rewrite the lock:

```bash
task lock
```

A fix comes with a test that fails without it; a new behaviour comes with tests covering it.

## Pre-commit hooks

[`.pre-commit-config.yaml`](.pre-commit-config.yaml) holds the hooks; `task setup` installs them, and they must pass before you push. To run them all, or one while iterating:

```bash
pre-commit run --all-files
```

```bash
pre-commit run <hook-id> --all-files
```

| Hook | Checks | Fix |
| --- | --- | --- |
| `spotless` | Java formatting (palantir-java-format) and the Gradle Kotlin scripts (ktlint) | rewrites the files: review and stage them |
| `yamllint` | YAML style, block style only | by hand, per the message |
| `hadolint`, `trivy-dockerfile` | the `Dockerfile` | by hand; never with an ignore comment |
| `actionlint-system` | the workflows, with shellcheck on their `run:` scripts | by hand, per the message |
| `renovate-config-validator` | `renovate.json`, rejecting deprecated keys | `npx --package renovate -- renovate-config-validator --migrate` |
| `no-co-authors` | no `Co-Authored-By:` or `Generated with` line in the commit message | rewrite the message |

`--no-verify` and `SKIP=` are not the fix: a red hook is a real finding. If a rule is wrong for this repository, change the configuration in the same pull request and say why.

## Continuous integration

| Workflow | Purpose | Reproduce locally |
| --- | --- | --- |
| [`quality.yaml`](.github/workflows/quality.yaml) | quality gate: hooks, unit tests, integration tests on the oldest supported and the latest Keycloak | `task lint`, `task test`, `task test:integration` |
| [`security.yaml`](.github/workflows/security.yaml) | Trivy scan of the locked dependencies, and daily of the published image | `trivy fs .` |
| [`build.yaml`](.github/workflows/build.yaml) | multi-arch image build and Trivy scan; pushes only when run by hand with `push`, or by a release | `task build` then `docker build .` |
| [`release.yaml`](.github/workflows/release.yaml) | on a `v*` tag: the image, and independently the GitHub release with the attested jar and its SBOM | — |
| [`scorecard.yaml`](.github/workflows/scorecard.yaml) | OpenSSF Scorecard grade of the repository | — |
| [`renovate.yaml`](.github/workflows/renovate.yaml) | daily dependency update pull requests, configured in [`renovate.json`](renovate.json) | `dryRun` input on a manual run |

The `quality` jobs cache Gradle's dependencies, written from `master` only, because Maven Central rate-limits the repeated downloads; jobs that publish never use the cache.

`quality` and `security` are the required checks; `build` only runs when a file that goes into the image changes. `security` also fails on a schedule, with no change of yours, when a new fixable CVE reaches a dependency or the published image.

Secrets, which pull requests from forks do not get:

- `DOCKERHUB_USERNAME` and `DOCKERHUB_TOKEN`: push the image to Docker Hub.
- `RENOVATE_TOKEN`: a fine-grained token with contents, pull requests, issues and workflows write and Dependabot alerts read, so that Renovate's pull requests trigger CI.

An image for another Keycloak release, without a new provider release: run `build.yaml` by hand from the release tag, with `push` and `keycloak_version`.

## Submitting a change

- Commit subjects are imperative and lowercase, with an optional conventional prefix: `fix: …`, `build: …`, `ci(docker): …`. `git log --oneline` shows the style.
- A pull request is green in CI, updates the documentation it affects, and carries one changelog label from [`.github/release.yaml`](.github/release.yaml) (`fix`, `feature`, `chore`, …), which sorts it in the release notes.

## Releasing

[`scripts/release.sh`](scripts/release.sh) is the only supported way to cut a release: it sets the version in `build.gradle.kts` and the README, commits, tags `v<version>`, and asks before pushing. From an up-to-date `master`:

```bash
task release -- 1.8.0
```

The tag runs `release.yaml`, which refuses a tag that disagrees with `build.gradle.kts`. A pre-release (`1.8.0-rc1`) publishes the image only, with no GitHub release.
