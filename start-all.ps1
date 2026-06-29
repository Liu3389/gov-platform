# ============================================
# 一键启动全环境 - Windows PowerShell
# 用法:
#   powershell -ExecutionPolicy Bypass -File start-all.ps1          # 完整启动
#   powershell -ExecutionPolicy Bypass -File start-all.ps1 -Quick   # 快速启动
# ============================================
$ErrorActionPreference = "Continue"
$ProjectDir = Split-Path -Parent $MyInvocation.MyCommand.Path

param(
    [switch]$Quick
)

Write-Host ""
Write-Host "========================================="
Write-Host "  智慧政务平台 — 一键全环境启动 (Windows)"
if ($Quick) {
    Write-Host "  模式: 快速启动（保留数据库）"
} else {
    Write-Host "  模式: 完整启动（重建数据库）"
}
Write-Host "========================================="

# ==================== Phase 1: Docker 基础设施 ====================
Write-Host ""
Write-Host "——— Phase 1/3: Docker 基础设施 ———"

if ($Quick) {
    Write-Host "[1] 启动 Docker 服务..."
    Set-Location "$ProjectDir\docker"
    docker compose up -d
} else {
    Write-Host "[1] 重建数据库（Flyway 迁移）..."
    & "$ProjectDir\db\reset.ps1"
}

Write-Host ""
Write-Host "  关键服务状态："
docker ps --format "  {{.Names}}: {{.Status}}" 2>$null | Select-String "gov-mysql|gov-nacos|gov-redis|gov-rabbitmq"

# 声明 RabbitMQ 队列（容器重建后队列丢失）
Write-Host ""
Write-Host "  RabbitMQ 队列声明..."
for ($i = 1; $i -le 10; $i++) {
  try {
    $resp = Invoke-WebRequest -Uri "http://localhost:15673/api/queues/%2f/message.send" `
      -Method Put -Headers @{"Authorization"="Basic "+[Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("guest:guest"))} `
      -ContentType "application/json" -Body '{"durable":true,"auto_delete":false}' `
      -SkipHttpErrorCheck -TimeoutSec 3
    if ($resp.StatusCode -eq 201 -or $resp.StatusCode -eq 204) {
      Write-Host "  √ 队列 message.send 已就绪"
      break
    }
  } catch {}
  if ($i -lt 10) { Start-Sleep 2 }
}
if ($resp.StatusCode -ne 201 -and $resp.StatusCode -ne 204) {
  Write-Host "  ! 队列声明失败（message-service 启动后会自行声明）"
}

# ==================== Phase 2: 编译所有模块 ====================
Write-Host ""
Write-Host "——— Phase 2/3: 编译所有微服务模块 ———"
Write-Host "[2] mvn clean package -DskipTests ..."
Set-Location $ProjectDir

mvn clean install -DskipTests -pl gov-common -q 2>&1 | Select-Object -Last 1
mvn clean package -DskipTests -q 2>&1 | Select-Object -Last 3

Write-Host "  √ 编译完成"

# ==================== Phase 3: 启动微服务 ====================
Write-Host ""
Write-Host "——— Phase 3/3: 启动微服务 ———"
Write-Host "  注意：以下服务在后台启动，请查看各模块的 logs\ 目录"

$services = @{
    "gov-gateway" = "8091"
    "gov-user-service" = "8081"
    "gov-item-service" = "8092"
    "gov-reception-service" = "8083"
    "gov-activiti-service" = "8084"
    "gov-license-service" = "8085"
    "gov-complaint-service" = "8086"
    "gov-open-service" = "8087"
    "gov-datashare-service" = "8088"
    "gov-message-service" = "8089"
    "gov-monitor-service" = "8090"
}

$started = 0
$failed = 0

foreach ($module in $services.Keys) {
    $port = $services[$module]
    $moduleDir = "$ProjectDir\$module"
    
    if (-not (Test-Path $moduleDir)) {
        Write-Host "  >> 跳过 $module（目录不存在）"
        continue
    }
    
    $jarFile = Get-ChildItem "$moduleDir\target\*.jar" -ErrorAction SilentlyContinue | Select-Object -First 1
    if (-not $jarFile) {
        Write-Host "  !! 跳过 $module（JAR 未找到，请先编译）"
        $failed++
        continue
    }
    
    $logDir = "$moduleDir\logs"
    New-Item -ItemType Directory -Force -Path $logDir | Out-Null
    
    # 杀掉旧进程
    $oldPid = (Get-NetTCPConnection -LocalPort $port -ErrorAction SilentlyContinue | Select-Object -First 1).OwningProcess
    if ($oldPid) {
        Stop-Process -Id $oldPid -Force -ErrorAction SilentlyContinue
        Start-Sleep -Seconds 1
    }
    
    Start-Process -NoNewWindow -FilePath "java" `
        -ArgumentList "-jar", "`"$($jarFile.FullName)`"", "--server.port=$port" `
        -RedirectStandardOutput "$logDir\app.log" `
        -RedirectStandardError "$logDir\error.log"
    
    Write-Host "  √ $module → 端口 $port"
    $started++
}

Write-Host ""
Write-Host "========================================="
Write-Host "  √ 全环境启动完成！"
Write-Host "  √ 启动: $started  |  !! 失败/跳过: $failed"
Write-Host "========================================="
Write-Host ""
Write-Host "关键地址："
Write-Host "  Nacos 控制台:  http://localhost:8848/nacos  (nacos/nacos)"
Write-Host "  Knife4j 文档:  http://localhost:8091/doc.html"
Write-Host "  RabbitMQ 管理:  http://localhost:15673  (guest/guest)"
Write-Host ""
Write-Host "查看某个服务日志："
Write-Host "  Get-Content gov-gateway\logs\app.log -Wait"
Write-Host ""
Write-Host "停止所有微服务："
Write-Host "  powershell -File stop-all.ps1"
Write-Host ""
