#!/bin/bash
# ============================================
# 一键停止所有微服务
# 用法: bash stop-all.sh
# ============================================
set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
PORTS=(8091 8081 8092 8083 8084 8085 8086 8087 8088 8089 8090)

echo "停止所有微服务..."

STOPPED=0
for PORT in "${PORTS[@]}"; do
  PID=$(lsof -ti:$PORT -sTCP:LISTEN | awk 'NR>1{print $2}' 2>/dev/null || true)
  if [ -n "$PID" ]; then
    kill "$PID" 2>/dev/null || true
    echo "  ✅ 端口 $PORT (PID: $PID) 已停止"
    ((STOPPED++))
  fi
done

echo ""
echo "停止 Docker 基础设施..."
cd "$PROJECT_DIR/docker"
docker compose stop 2>/dev/null || true

echo ""
echo "✅ 已停止 $STOPPED 个微服务 + Docker 基础设施"
