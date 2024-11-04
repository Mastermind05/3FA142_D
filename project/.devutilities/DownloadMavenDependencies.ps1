# Set variables for your project
$projectPath = "C:\Users\Schueler\Workspace\3FA142_D\project"
$mavenHome = [System.Environment]::GetEnvironmentVariable("MAVEN_HOME", "User")

# Change to project directory
Set-Location $projectPath

# Function to run a command and handle errors
function Run-Command {
    param (
        [string]$command,
        [string]$errorMessage
    )
    
    try {
        Invoke-Expression $command
    } catch {
        Write-Host "Error: $errorMessage" -ForegroundColor Red
        Write-Host $_.Exception.Message -ForegroundColor Yellow
    }
}

# Step 1: Download Maven dependencies
Write-Host "Downloading Maven dependencies..." -ForegroundColor Green
Run-Command "mvn clean install" "Failed to download Maven dependencies."

# Step 2: Build the Docker environment
Write-Host "Building Docker environment..." -ForegroundColor Green
Run-Command "docker-compose -f .\docker\docker-compose.yml up --build" "Failed to build Docker environment."
