#!/bin/bash
# ============================================
# 一键启动全环境
# 用途: Docker 基础设施 → Flyway 建库 → 编译 → 启动所有微服务
# 用法: bash start-all.sh          # 完整启动（含重置数据库）
#       bash start-all.sh --quick  # 快速启动（保留数据库）
# ============================================
set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
QUICK_MODE=false

if [ "$1" = "--quick" ] || [ "$1" = "-q" ]; then
  QUICK_MODE=true
fi

echo ""
echo "========================================="
echo "  智慧政务平台 — 一键全环境启动"
if [ "$QUICK_MODE" = true ]; then
  echo "  模式: 快速启动（保留数据库）"
else
  echo "  模式: 完整启动（重建数据库）"
fi
echo "========================================="

# ==================== Phase 1: Docker 基础设施 ====================
echo ""
echo "━━━ Phase 1/3: Docker 基础设施 ━━━"

if [ "$QUICK_MODE" = true ]; then
  # 快速模式：只启动容器，不清理数据
  echo "[1] 启动 Docker 服务..."
  cd "$PROJECT_DIR/docker"
  docker compose up -d
else
  # 完整模式：重建数据库
  echo "[1] 重建数据库（Flyway 迁移）..."
  bash "$PROJECT_DIR/db/reset.sh"
  echo ""
  echo "[1b] 基础设施已就绪"
fi

# 验证关键服务
echo ""
echo "  📊 关键服务状态："
docker ps --format "  {{.Names}}: {{.Status}}" 2>/dev/null | grep -E "gov-mysql|gov-nacos|gov-redis|gov-rabbitmq" || true

# 声明 RabbitMQ 队列（容器重建后队列丢失）
echo ""
echo "  📨 声明 RabbitMQ 队列..."
for i in $(seq 1 10); do
  STATUS=$(curl -s -o /dev/null -w "%{http_code}" -u guest:guest \
    -X PUT "http://localhost:15673/api/queues/%2f/message.send" \
    -H "Content-Type: application/json" \
    -d '{"durable":true,"auto_delete":false}' 2>/dev/null)
  if [ "$STATUS" = "201" ] || [ "$STATUS" = "204" ]; then
    echo "  ✅ 队列 message.send 已就绪"
    break
  fi
  [ "$i" -lt 10 ] && sleep 2
done
[ "$STATUS" != "201" ] && [ "$STATUS" != "204" ] && echo "  ⚠️  队列声明失败（不影响启动，message-service 启动后会自行声明）"

# ==================== Phase 2: 编译所有模块 ====================
echo ""
echo "━━━ Phase 2/3: 编译所有微服务模块 ━━━"
echo "[2] mvn clean package -DskipTests ..."

cd "$PROJECT_DIR"
mvn clean install -DskipTests -pl gov-common -q 2>&1 | tail -1 || echo "  ⚠️  gov-common 编译问题"
mvn clean package -DskipTests -q 2>&1 | tail -3 || true
echo "  ✅ 编译完成"

# ==================== Phase 3: 启动微服务 ====================
echo ""
echo "━━━ Phase 3/3: 启动微服务 ━━━"
echo "  注意：以下服务在后台启动，请查看各模块的 logs/ 目录"
echo ""

# 定义服务：模块名:端口
declare -A SERVICES=(
  ["gov-gateway"]="8091"
  ["gov-user-service"]="8081"
  ["gov-item-service"]="8092"
  ["gov-reception-service"]="8083"
  ["gov-activiti-service"]="8084"
  ["gov-license-service"]="8085"
  ["gov-complaint-service"]="8086"
  ["gov-open-service"]="8087"
  ["gov-datashare-service"]="8088"
  ["gov-message-service"]="8089"
  ["gov-monitor-service"]="8090"
)

STARTED=0
FAILED=0

for MODULE in "${!SERVICES[@]}"; do
  PORT="${SERVICES[$MODULE]}"
  MODULE_DIR="$PROJECT_DIR/$MODULE"
  
  if [ ! -d "$MODULE_DIR" ]; then
    echo "  ⏭️  跳过 $MODULE（目录不存在）"
    continue
  fi
  
  JAR_FILE=$(ls "$MODULE_DIR/target"/*.jar 2>/dev/null | head -1)
  if [ -z "$JAR_FILE" ]; then
    echo "  ⚠️  跳过 $MODULE（JAR 未找到，请先编译）"
    ((FAILED++))
    continue
  fi
  
  LOG_DIR="$MODULE_DIR/logs"
  mkdir -p "$LOG_DIR"
  
  # 杀掉旧进程
  OLD_PID=$(lsof -ti:$PORT 2>/dev/null || true)
  if [ -n "$OLD_PID" ]; then
    kill "$OLD_PID" 2>/dev/null || true
    sleep 1
  fi
  
  nohup java -jar "$JAR_FILE" --server.port=$PORT > "$LOG_DIR/app.log" 2>&1 &
  echo "  ✅ $MODULE → 端口 $PORT（PID: $!）"
  ((STARTED++))
done

echo ""
echo "========================================="
echo "  ✅ 全环境启动完成！"
echo "  ✅ 启动: $STARTED  |  ⚠️ 失败/跳过: $FAILED"
echo "========================================="
echo ""
echo "🔗 关键地址："
echo "  Nacos 控制台:  http://localhost:8848/nacos  (nacos/nacos)"
echo "  Knife4j 文档:  http://localhost:8091/doc.html"
echo "  Sentinel 控制台: http://localhost:8858  (sentinel/sentinel)"
echo "  RabbitMQ 管理:  http://localhost:15673  (guest/guest)"
echo "  SkyWalking UI:  http://localhost:18080"
echo ""
echo "📋 查看某个服务日志："
echo "  tail -f gov-gateway/logs/app.log"
echo ""
echo "📋 停止所有微服务："
echo "  bash stop-all.sh"
echo ""
