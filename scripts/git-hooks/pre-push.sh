#!/bin/sh

# Run tests
./gradlew test

# Check if tests passed
if [ $? -ne 0 ]; then
  echo "Tests failed, push aborted."
  exit 1
fi