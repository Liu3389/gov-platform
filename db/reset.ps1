# ============================================
# 一键重建数据库（Flyway 版本管理）- Windows PowerShell
# 用法: powershell -ExecutionPolicy Bypass -File db/reset.ps1
# ============================================
$ErrorActionPreference = "Stop"
$ProjectDir = Split-Path -Parent (Split-Path -Parent $MyInvocation.MyCommand.Path)
Set-Location "$ProjectDir\docker"

Write-Host ""
Write-Host "========================================="
Write-Host "  智慧政务平台 — 一键数据库重建 (Windows)"
Write-Host "  Flyway 自动版本管理"
Write-Host "========================================="
Write-Host ""

# ==================== Step 1: 停止 + 清空 ====================
Write-Host "[1/4] 停止所有容器并清除数据卷..."
docker compose down -v 2>$null
Write-Host "  √ 旧容器和数据卷已清理"

# ==================== Step 2: 启动 MySQL ====================
Write-Host ""
Write-Host "[2/4] 启动 MySQL（等待健康检查）..."
docker compose up -d mysql
for ($i = 1; $i -le 60; $i++) {
    $status = docker inspect gov-mysql --format='{{.State.Health.Status}}' 2>$null
    if ($status -eq "healthy") {
        Write-Host "  √ MySQL 就绪（${i}s）"
        break
    }
    if ($i -ge 60) {
        Write-Host "  × MySQL 启动超时"
        exit 1
    }
    Write-Host -NoNewline "."
    Start-Sleep -Seconds 2
}

# ==================== Step 3: 运行 Flyway 迁移 ====================
Write-Host ""
Write-Host "[3/4] Flyway 数据库迁移..."
docker run --rm `
  --network docker_gov-net `
  -v "${ProjectDir}\db\schema:/flyway/schema:ro" `
  -v "${ProjectDir}\db\data:/flyway/data:ro" `
  -v "${ProjectDir}\db\flyway.conf:/flyway/conf/flyway.conf:ro" `
  flyway/flyway:10-alpine migrate

Write-Host "  √ Flyway 迁移完成"

# ==================== Step 4: 启动全部基础设施 + Nacos 初始化 ====================
Write-Host ""
Write-Host "[4/4] 启动全部基础设施..."
docker compose up -d

# 等待 Nacos
Write-Host "  等待 Nacos 就绪..."
for ($i = 1; $i -le 90; $i++) {
    $status = docker inspect gov-nacos --format='{{.State.Health.Status}}' 2>$null
    if ($status -eq "healthy") {
        break
    }
    Write-Host -NoNewline "."
    Start-Sleep -Seconds 2
}

# Nacos 初始化
docker rm -f gov-nacos-init 2>$null
docker compose up -d nacos-init
Start-Sleep -Seconds 5
$initLog = docker logs gov-nacos-init 2>&1
if ($initLog -match "Nacos 初始化完成") {
    Write-Host "  √ Nacos 初始化完成"
} else {
    Write-Host "  ! Nacos 初始化可能未完成"
}

Write-Host ""
Write-Host "========================================="
Write-Host "  √ 数据库重建完成！"
Write-Host "========================================="
Write-Host ""
Write-Host "数据库验证："
docker exec gov-mysql mysql -uroot -proot123456 -N -e `
  "SELECT table_schema AS DB, COUNT(*) AS tables FROM information_schema.tables WHERE table_schema LIKE 'gov_%' GROUP BY table_schema ORDER BY table_schema;" 2>$null

Write-Host ""
Write-Host "RabbitMQ 队列声明（容器重建后队列丢失）..."
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

Write-Host ""
Write-Host "后续步骤："
Write-Host "  1. 在 IDEA 启动网关 → gov-gateway（8091）"
Write-Host "  2. 启动你负责的服务"
Write-Host "  3. Knife4j: http://localhost:8091/doc.html"
Write-Host ""
