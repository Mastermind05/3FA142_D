# Get the current user's username
$username = [System.Security.Principal.WindowsIdentity]::GetCurrent().Name.Split('\')[1]

# Define the Maven installation directory based on the username
$mavenHome = "C:\Users\$username\apache-maven\apache-maven-3.9.5"
$mavenBinPath = "$mavenHome\bin"

# Set MAVEN_HOME Environment Variable
[System.Environment]::SetEnvironmentVariable("MAVEN_HOME", $mavenHome, "User")

# Update Path Environment Variable
$oldPath = [System.Environment]::GetEnvironmentVariable("Path", "User")
$newPath = "$oldPath;$mavenBinPath"
[System.Environment]::SetEnvironmentVariable("Path", $newPath, "User")

# Verify the changes
Write-Output "MAVEN_HOME set to: $([System.Environment]::GetEnvironmentVariable('MAVEN_HOME', 'User'))"
Write-Output "Path updated to include Maven bin directory."

# Instructions for verifying installation
Write-Output "Please restart PowerShell and run 'mvn -v' to verify Maven installation."
