@echo off

:: Ensure the current branch is not 'main' or 'dev'
for /f "tokens=*" %%i in ('git rev-parse --abbrev-ref HEAD') do set current_branch=%%i
if "%current_branch%"=="main" (
    echo Cannot merge directly into 'main' branch.
    exit /b 1
) else if "%current_branch%"=="dev" (
    echo Cannot merge directly into 'dev' branch.
    exit /b 1
)

:: Run the build (includes compiling, testing, etc.)
call ./gradlew build
if %ERRORLEVEL% neq 0 (
    echo Build failed, merge aborted.
    exit /b 1
)

:: Check if the target branches 'main' and 'dev' are up-to-date
for %%b in (main dev) do (
    git fetch origin %%b
    for /f "tokens=*" %%c in ('git rev-list HEAD...origin/%%b --count') do set commits_behind=%%c
    if %commits_behind% neq 0 (
        echo '%%b' branch is not up-to-date, please pull the latest changes.
        exit /b 1
    )
)

:: Ensure no uncommitted changes are present
git diff-index --quiet HEAD --
if %ERRORLEVEL% neq 0 (
    echo There are uncommitted changes, please commit or stash them before merging.
    exit /b 1
)

echo Pre-merge checks passed, you can proceed with the merge.
