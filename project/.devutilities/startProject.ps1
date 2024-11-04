# Define the project path
$projectPath = "C:\Users\Schueler\Workspace\3FA142_D\project"

# Navigate to the project directory
Set-Location -Path $projectPath

# Check if Maven is installed
if (-not (Get-Command mvn -ErrorAction SilentlyContinue)) {
    Write-Host "Maven is not installed. Please install Maven before running this script."
    exit
}

# Download Maven dependencies
Write-Host "Downloading Maven dependencies..."
mvn clean install

if ($LASTEXITCODE -ne 0) {
    Write-Host "Maven build failed. Please check the output for errors."
    exit
}

# Check if Docker is installed
if (-not (Get-Command docker -ErrorAction SilentlyContinue)) {
    Write-Host "Docker is not installed. Please install Docker before running this script."
    exit
}

# Build Docker environment using Docker Compose
Write-Host "Building Docker environment..."
docker-compose -f .\docker\docker-compose.yml up --build -d

if ($LASTEXITCODE -ne 0) {
    Write-Host "Docker build failed. Please check the output for errors."
    exit
}

Write-Host "Build completed successfully."
