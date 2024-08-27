@echo off

setlocal

set HOOKS_DIR=scripts\git-hooks\bat
set GIT_HOOKS_DIR=.git\hooks

:: Check if the .git/hooks directory exists
if not exist "%GIT_HOOKS_DIR%" (
    echo .git\hooks directory does not exist. Please make sure you are running this script in the correct directory.
    exit /b 1
)

:: Copy hooks to .git/hooks
for %%f in (%HOOKS_DIR%\*) do (
    copy "%%f" "%GIT_HOOKS_DIR%\%%~nxf"
)

:: Make the scripts executable (this is generally unnecessary for Windows)
echo Git hooks have been installed.

endlocal
