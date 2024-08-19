#!/bin/sh

# Run Spotless to format code
./gradlew spotlessApply

# Add any changes made by Spotless
git add .
