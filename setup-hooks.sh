#!/bin/bash

HOOKS_DIR="scripts/git-hooks"
GIT_HOOKS_DIR=".git/hooks"

# Copy hooks to .git/hooks
for hook in "$HOOKS_DIR"/*; do
  hook_name=$(basename "$hook")
  cp "$hook" "$GIT_HOOKS_DIR/$hook_name"
  chmod +x "$GIT_HOOKS_DIR/$hook_name"
done

echo "Git hooks have been installed."
