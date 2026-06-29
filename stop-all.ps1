# ============================================
# 一键停止所有微服务 - Windows PowerShell
# 用法: powershell -ExecutionPolicy Bypass -File stop-all.ps1
# ============================================
$ErrorActionPreference = "Continue"
$ProjectDir = Split-Path -Parent $MyInvocation.MyCommand.Path

$ports = @(8091, 8081, 8092, 8083, 8084, 8085, 8086, 8087, 8088, 8089, 8090)

Write-Host "停止所有微服务..."

$stopped = 0
foreach ($port in $ports) {
    $conn = Get-NetTCPConnection -LocalPort $port -ErrorAction SilentlyContinue | Select-Object -First 1
    if ($conn) {
        $pid = $conn.OwningProcess
        Stop-Process -Id $pid -Force -ErrorAction SilentlyContinue
        Write-Host "  √ 端口 $port (PID: $pid) 已停止"
        $stopped++
    }
}

Write-Host ""
Write-Host "停止 Docker 基础设施..."
Set-Location "$ProjectDir\docker"
docker compose stop 2>$null

Write-Host ""
Write-Host "√ 已停止 $stopped 个微服务 + Docker 基础设施"
Write-Host ""
