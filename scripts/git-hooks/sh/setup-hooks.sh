#!/bin/bash

SCRIPT_DIR=$(dirname "$(realpath "$0")")

HOOKS_DIR="$SCRIPT_DIR"
GIT_HOOKS_DIR="$(git rev-parse --git-dir)/hooks"

for hook in "$HOOKS_DIR"/*; do
  hook_name=$(basename "$hook")
  cp "$hook" "$GIT_HOOKS_DIR/$hook_name"
  chmod +x "$GIT_HOOKS_DIR/$hook_name"
done

echo "Git hooks have been installed."