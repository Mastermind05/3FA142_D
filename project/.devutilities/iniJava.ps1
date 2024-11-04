# Set variables for the JDK installation
$jdkVersion = "21"
$jdkFolderName = "jdk-$jdkVersion"
$jdkDownloadUrl = "https://download.oracle.com/java/21/archive/jdk-21_windows-x64_bin.exe"
$jdkInstallerPath = "C:\Users\$env:USERNAME\Downloads\$jdkFolderName.exe"
$installPath = "C:\Program Files\Java\$jdkFolderName"

# Function to check if JDK is already installed
function Check-JDKInstalled {
    $jdkInstalled = Get-Command "java" -ErrorAction SilentlyContinue
    if ($jdkInstalled) {
        $javaHome = [System.Environment]::GetEnvironmentVariable("JAVA_HOME", "Machine")
        if ($javaHome -and (Test-Path $javaHome)) {
            Write-Host "JDK is already installed at: $javaHome" -ForegroundColor Green
            exit
        }
    }
}

# Function to download JDK installer
function Download-JDKInstaller {
    Write-Host "Downloading JDK 21 installer..." -ForegroundColor Green
    try {
        Invoke-WebRequest -Uri $jdkDownloadUrl -OutFile $jdkInstallerPath
        Write-Host "JDK 21 installer downloaded successfully." -ForegroundColor Green
    } catch {
        Write-Host "Failed to download JDK installer: $_" -ForegroundColor Red
        exit
    }
}

# Function to install JDK silently
function Install-JDK {
    Write-Host "Installing JDK 21..." -ForegroundColor Green
    try {
        Start-Process -FilePath $jdkInstallerPath -ArgumentList "/s", "INSTALLDIR=$installPath" -Wait -NoNewWindow
        Write-Host "JDK 21 installed successfully." -ForegroundColor Green
    } catch {
        Write-Host "Failed to install JDK: $_" -ForegroundColor Red
        exit
    }
}

# Function to set JAVA_HOME and update PATH
function Set-JavaEnvironmentVariables {
    Write-Host "Setting JAVA_HOME and updating PATH..." -ForegroundColor Green
    try {
        [System.Environment]::SetEnvironmentVariable("JAVA_HOME", $installPath, "Machine")
        $currentPath = [System.Environment]::GetEnvironmentVariable("Path", "Machine")
        if (-not $currentPath.Split(';') -contains "$installPath\bin") {
            [System.Environment]::SetEnvironmentVariable("Path", "$currentPath;$installPath\bin", "Machine")
        }
        Write-Host "Environment variables set successfully." -ForegroundColor Green
    } catch {
        Write-Host "Failed to set environment variables: $_" -ForegroundColor Red
        exit
    }
}

# Main execution flow
Check-JDKInstalled
Download-JDKInstaller
Install-JDK
Set-JavaEnvironmentVariables

Write-Host "Installation process completed. Please restart PowerShell or your computer to apply changes." -ForegroundColor Yellow
