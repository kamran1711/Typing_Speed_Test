#!/usr/bin/env bash
# Quick wrapper for Typing Speed Test project
if command -v mvn &> /dev/null; then
    exec mvn "$@"
elif [ -x "/opt/homebrew/bin/mvn" ]; then
    exec /opt/homebrew/bin/mvn "$@"
elif [ -x "$HOME/.maven/apache-maven-3.9.6/bin/mvn" ]; then
    exec "$HOME/.maven/apache-maven-3.9.6/bin/mvn" "$@"
else
    echo "Maven not found in PATH, /opt/homebrew/bin/mvn, or ~/.maven."
    exit 1
fi
