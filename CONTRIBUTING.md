# Contributing

Questions and bugs go in [GitHub issues](https://github.com/leroyguillaume/keycloak-bcrypt/issues), changes in pull requests. The short version: `mise install`, `task setup`, then `task lint` and `task test` green before you push.

## Development setup

[mise](https://mise.jdx.dev/getting-started.html) installs every tool the project needs — the JDK, Task and pre-commit — at the versions pinned in [`mise.toml`](mise.toml). Gradle comes from the wrapper. Running Keycloak locally also needs Docker.

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
| `no-co-authors` | no `Co-Authored-By:` or `Generated with` line in the commit message | rewrite the message |

`--no-verify` and `SKIP=` are not the fix: a red hook is a real finding. If a rule is wrong for this repository, change the configuration in the same pull request and say why.

## Continuous integration

| Workflow | Purpose | Reproduce locally |
| --- | --- | --- |
| [`quality.yml`](.github/workflows/quality.yml) | unit tests | `task test` |
| [`release.yml`](.github/workflows/release.yml) | GitHub release with the jar, on a tag | `task build` |
| [`docker.yml`](.github/workflows/docker.yml) | image build and push to Docker Hub, run by hand with a Keycloak version | `docker build .` |

`docker.yml` needs the `DOCKERHUB_USERNAME` and `DOCKERHUB_TOKEN` secrets to push.

## Submitting a change

- Commit subjects are imperative and lowercase, with an optional conventional prefix: `fix: …`, `build: …`, `ci(docker): …`. `git log --oneline` shows the style.
- A pull request is green in CI and updates the documentation it affects.

## Releasing

Set `version` in [`build.gradle.kts`](build.gradle.kts), then push a `v<version>` tag: `release.yml` creates the GitHub release with the jar. Run `docker.yml` with the Keycloak version the image must be based on.
