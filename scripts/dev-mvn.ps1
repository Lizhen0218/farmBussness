param(
    [Parameter(ValueFromRemainingArguments = $true)]
    [string[]] $MavenArgs
)

$ErrorActionPreference = "Stop"

$jdk = "C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot"
$mvn = "D:\lcInstall\Maven\apache-maven-3.6.3\bin\mvn.cmd"
$settings = "D:\Farm\code\farm-server\settings-workspace.xml"

if (-not (Test-Path -LiteralPath "$jdk\bin\java.exe")) {
    throw "JDK 21 not found at $jdk"
}

if (-not (Test-Path -LiteralPath $mvn)) {
    throw "Maven not found at $mvn"
}

$env:JAVA_HOME = $jdk
$env:Path = "$jdk\bin;$env:Path"

& $mvn -s $settings @MavenArgs
