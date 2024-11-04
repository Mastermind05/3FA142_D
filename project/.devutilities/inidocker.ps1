# Path to your docker-compose.yml file
$dockerComposePath = "C:\Users\Schueler\Workspace\3FA142_D\project\docker\docker-compose.yml"

# Check if Docker is installed
function Check-DockerInstalled {
    $dockerPath = Get-Command docker -ErrorAction SilentlyContinue
    if (-not $dockerPath) {
        Write-Host "Docker is not installed. Please install Docker first." -ForegroundColor Red
        exit
    } else {
        Write-Host "Docker is installed." -ForegroundColor Green
    }
}

# Check if Docker is running
function Check-DockerRunning {
    try {
        $dockerInfo = & docker info
        Write-Host "Docker is running." -ForegroundColor Green
    } catch {
        Write-Host "Docker is not running. Starting Docker..." -ForegroundColor Yellow
        Start-Process "C:\Program Files\Docker\Docker\Docker Desktop.exe" # Adjust the path if necessary
        Start-Sleep -Seconds 10 # Wait for Docker to start

        # Verify if Docker started
        try {
            $dockerInfo = & docker info
            Write-Host "Docker started successfully." -ForegroundColor Green
        } catch {
            Write-Host "Failed to start Docker. Please check Docker installation." -ForegroundColor Red
            exit
        }
    }
}

# Build Docker environment using docker-compose
function Build-DockerEnvironment {
    if (-Not (Test-Path $dockerComposePath)) {
        Write-Host "docker-compose.yml file not found at path: $dockerComposePath" -ForegroundColor Red
        exit
    }

    Write-Host "Building Docker environment using docker-compose..." -ForegroundColor Green
    try {
        & docker-compose -f $dockerComposePath up --build -d
        Write-Host "Docker environment built successfully." -ForegroundColor Green
    } catch {
        Write-Host "Failed to build Docker environment: $_" -ForegroundColor Red
        exit
    }
}

# Main script execution
Check-DockerInstalled
Check-DockerRunning
Build-DockerEnvironment
