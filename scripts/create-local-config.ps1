param(
    [string] $MysqlHost = "127.0.0.1",
    [string] $MysqlPort = "3306",
    [string] $MysqlDatabase = "farm_game",
    [string] $MysqlUsername = "root",
    [string] $RedisHost = "127.0.0.1",
    [string] $RedisPort = "6379",
    [string] $RedisDatabase = "0"
)

$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$configDir = Join-Path $projectRoot "config"
$configFile = Join-Path $configDir "application-local.yml"

New-Item -ItemType Directory -Force -Path $configDir | Out-Null

$mysqlPassword = Read-Host "请输入本地 MySQL 密码"
$redisPassword = Read-Host "请输入本地 Redis 密码，如果没有密码直接回车"

$content = @"
spring:
  datasource:
    url: jdbc:mysql://$MysqlHost`:$MysqlPort/$MysqlDatabase?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: $MysqlUsername
    password: $mysqlPassword
  data:
    redis:
      host: $RedisHost
      port: $RedisPort
      password: $redisPassword
      database: $RedisDatabase
"@

Set-Content -LiteralPath $configFile -Value $content -Encoding UTF8
Write-Host "已生成本机配置：$configFile"
Write-Host "该文件已被 .gitignore 忽略，不会提交到仓库。"
