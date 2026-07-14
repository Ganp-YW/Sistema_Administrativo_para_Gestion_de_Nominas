$libDir = ".\lib"
if (!(Test-Path -Path $libDir)) {
    New-Item -ItemType Directory -Path $libDir
}

$dependencies = @(
    "https://repo1.maven.org/maven2/io/zonky/test/embedded-postgres/2.0.7/embedded-postgres-2.0.7.jar",
    "https://repo1.maven.org/maven2/org/apache/commons/commons-compress/1.26.1/commons-compress-1.26.1.jar",
    "https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar",
    "https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/1.7.36/slf4j-simple-1.7.36.jar",
    "https://repo1.maven.org/maven2/org/tukaani/xz/1.9/xz-1.9.jar",
    "https://repo1.maven.org/maven2/org/apache/commons/commons-lang3/3.14.0/commons-lang3-3.14.0.jar",
    "https://repo1.maven.org/maven2/commons-io/commons-io/2.15.1/commons-io-2.15.1.jar",
    "https://repo1.maven.org/maven2/commons-codec/commons-codec/1.16.1/commons-codec-1.16.1.jar"
)

foreach ($url in $dependencies) {
    $fileName = Split-Path $url -Leaf
    $dest = Join-Path $libDir $fileName
    if (!(Test-Path -Path $dest)) {
        Write-Host "Downloading $fileName..."
        Invoke-WebRequest -Uri $url -OutFile $dest
        Write-Host "Downloaded $fileName"
    } else {
        Write-Host "$fileName already exists, skipping."
    }
}

Write-Host "All dependencies downloaded successfully!"
