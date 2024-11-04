# Define Maven version and download URL
$mavenVersion = "3.9.5"
$downloadUrl = "https://downloads.apache.org/maven/maven-3/$mavenVersion/binaries/apache-maven-$mavenVersion-bin.zip"
$zipPath = "C:\apache-maven-$mavenVersion-bin.zip"
$installPath = "C:\Program Files\Apache\maven"

# Create the download directory if it doesn't exist
New-Item -ItemType Directory -Path "C:\Program Files\Apache" -Force

# Download the Maven ZIP file
Invoke-WebRequest -Uri $downloadUrl -OutFile $zipPath

# Extract the Maven ZIP file
Expand-Archive -Path $zipPath -DestinationPath $installPath -Force

# Set MAVEN_HOME environment variable
[System.Environment]::SetEnvironmentVariable("MAVEN_HOME", "$installPath\apache-maven-$mavenVersion", "Machine")

# Update the Path environment variable
$oldPath = [System.Environment]::GetEnvironmentVariable("Path", "Machine")
$newPath = "$oldPath;$installPath\apache-maven-$mavenVersion\bin"
[System.Environment]::SetEnvironmentVariable("Path", $newPath, "Machine")

# Verify installation (run this command in a new PowerShell window)
Write-Output "Maven has been installed. Please open a new PowerShell window and run 'mvn -v' to verify."
