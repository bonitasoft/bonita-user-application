#!/usr/bin/env bash
#
#
# This script accepts the following parameters:
#
# * tag
# * prerelease
# * github_api_token
#
# Script to upload a release asset using the GitHub API v3.
#
# Example:
#
# create-github-release.sh github_api_token=TOKEN tag=v0.1.0 prerelease=false
#

# Check dependencies.
set -e
xargs=$(which gxargs || which xargs)

# Validate settings.
[ "$TRACE" ] && set -x

CONFIG=$@

for line in $CONFIG; do
  eval "$line"
done

# Define variables.
GH_API="https://api.github.com"
GH_REPO="$GH_API/repos/bonitasoft/bonita-user-application"
GH_REPO_RELEASE="$GH_REPO/releases"
AUTH="Authorization: token $github_api_token"
WGET_ARGS="--content-disposition --auth-no-challenge --no-cookie"
CURL_ARGS="-LJO#"

# Validate token.
curl -o /dev/null -sH "$AUTH" $GH_REPO || { echo "Error: Invalid repo, token or network issue!";  exit 1; }

curl -X POST -H "$AUTH" -H "Content-Type: application/json" --data '{ "tag_name": "'$tag'", "prerelease": '$prerelease' }' $GH_REPO_RELEASE 
