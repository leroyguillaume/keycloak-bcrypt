#!/usr/bin/env bash
set -euo pipefail

usage() {
  cat <<'USAGE'
Usage: scripts/release.sh [--dry-run] <version>

Bump the provider version in build.gradle.kts and the README, commit, create
the annotated v<version> tag, then ask before pushing. Pushing the tag runs
the release workflow, which publishes the jar and the image.

Examples:
  scripts/release.sh 1.8.0
  scripts/release.sh 1.8.0-rc1
  scripts/release.sh --dry-run 1.8.0
USAGE
}

die() {
  echo "error: $*" >&2
  exit 1
}

dry_run=false
version=""
while [ $# -gt 0 ]; do
  case "$1" in
    -h | --help)
      usage
      exit 0
      ;;
    --dry-run) dry_run=true ;;
    -*) die "unknown option $1" ;;
    *)
      [ -z "$version" ] || die "one version only"
      version="$1"
      ;;
  esac
  shift
done

[ -n "$version" ] || {
  usage >&2
  exit 1
}
[[ "$version" =~ ^[0-9]+\.[0-9]+\.[0-9]+(-[0-9A-Za-z.]+)?$ ]] || die "$version is not a semver version"

cd "$(git rev-parse --show-toplevel)"

branch="master"
tag="v${version}"
current="$(sed -n 's/^version = "\(.*\)"$/\1/p' build.gradle.kts)"
keycloak="$(sed -n 's/^ARG KEYCLOAK_IMAGE=[^:]*:\([^@]*\).*$/\1/p' Dockerfile)"
stable=true
[[ "$version" == *-* ]] && stable=false

if $dry_run; then
  echo "version: ${current} -> ${version}"
  echo "commit:  release ${version}"
  echo "tag:     ${tag}"
  $stable && echo "README:  jar ${version}, image ${keycloak}_${version}"
  exit 0
fi

[ -z "$(git status --porcelain)" ] || die "the working tree is not clean"
[ "$(git rev-parse --abbrev-ref HEAD)" = "$branch" ] || die "releases are cut from ${branch}"
git fetch --quiet --tags origin "$branch"
[ "$(git rev-list --count "HEAD..origin/${branch}")" = 0 ] || die "${branch} is behind origin: pull first"
! git rev-parse --quiet --verify "refs/tags/${tag}" >/dev/null || die "tag ${tag} already exists"

sed -i.bak "s/^version = \".*\"$/version = \"${version}\"/" build.gradle.kts
rm build.gradle.kts.bak
if $stable; then
  sed -i.bak -E \
    -e "s#releases/download/v[0-9][^/]*/keycloak-bcrypt-[^/ ]+\.jar#releases/download/${tag}/keycloak-bcrypt-${version}.jar#g" \
    -e "s#keycloak-bcrypt-[0-9][0-9A-Za-z.-]*\.jar#keycloak-bcrypt-${version}.jar#g" \
    -e "s#(keycloak-bcrypt):[0-9][0-9.]*_[0-9][0-9A-Za-z.-]*#\1:${keycloak}_${version}#g" \
    README.md
  rm README.md.bak
fi

git commit --quiet --all --message "release ${version}"
git tag --annotate "$tag" --message "$tag"
echo "committed and tagged ${tag}"

push="git push --atomic origin ${branch} ${tag}"
if [ ! -t 0 ]; then
  echo "no terminal: not pushing. Publish with: ${push}"
  exit 0
fi
printf 'push %s and %s to origin? this publishes the release. [y/N] ' "$branch" "$tag"
read -r reply
case "$reply" in
  y | Y | yes) $push ;;
  *) echo "not pushed. Publish later with: ${push}" ;;
esac
