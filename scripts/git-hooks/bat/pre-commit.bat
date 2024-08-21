@echo off

:: Run Ktlint to format code
call ./gradlew ktlintFormat

:: Add any changes made by Spotless
git add .

:: Optional: Indicate completion
echo Code formatting complete and changes added.
