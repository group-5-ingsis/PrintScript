@echo off

:: Run tests & linter (~Checkstyle)
call ./gradlew ktlintCheck
call ./gradlew test

:: Check if tests passed
if %ERRORLEVEL% neq 0 (
    echo Tests failed, push aborted.
    exit /b 1
)
