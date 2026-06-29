#!/bin/bash
# ============================================
# 一键重建数据库（Flyway 版本管理）
# 用途: 停止旧容器 → 清空数据卷 → 重建 → Flyway 迁移 → Nacos 初始化
# 用法: bash db/reset.sh
# ============================================
set -e

PROJECT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
cd "$PROJECT_DIR/docker"

echo ""
echo "========================================="
echo "  智慧政务平台 — 一键数据库重建"
echo "  Flyway 自动版本管理"
echo "========================================="
echo ""

# ==================== Step 1: 停止 + 清空 ====================
echo "[1/4] 停止所有容器并清除数据卷..."
docker compose down -v 2>/dev/null
echo "  ✅ 旧容器和数据卷已清理"

# ==================== Step 2: 启动 MySQL ====================
echo ""
echo "[2/4] 启动 MySQL（等待健康检查）..."
docker compose up -d mysql
for i in $(seq 1 60); do
  STATUS=$(docker inspect gov-mysql --format='{{.State.Health.Status}}' 2>/dev/null || echo "unknown")
  if [ "$STATUS" = "healthy" ]; then
    echo "  ✅ MySQL 就绪（${i}s）"
    break
  fi
  [ $i -ge 60 ] && echo "  ❌ MySQL 启动超时" && exit 1
  echo -n "."
  sleep 2
done

# ==================== Step 3: 运行 Flyway 迁移 ====================
echo ""
echo "[3/4] Flyway 数据库迁移..."
docker run --rm \
  --network docker_gov-net \
  -v "$PROJECT_DIR/db/schema:/flyway/schema:ro" \
  -v "$PROJECT_DIR/db/data:/flyway/data:ro" \
  -v "$PROJECT_DIR/db/flyway.conf:/flyway/conf/flyway.conf:ro" \
  flyway/flyway:10-alpine migrate

echo "  ✅ Flyway 迁移完成"

# ==================== Step 4: 启动全部基础设施 + Nacos 初始化 ====================
echo ""
echo "[4/4] 启动全部基础设施..."
docker compose up -d

# 等待 Nacos
echo "  等待 Nacos 就绪..."
for i in $(seq 1 90); do
  STATUS=$(docker inspect gov-nacos --format='{{.State.Health.Status}}' 2>/dev/null || echo "unknown")
  if [ "$STATUS" = "healthy" ]; then
    break
  fi
  echo -n "."
  sleep 2
done

# Nacos 初始化
docker rm -f gov-nacos-init 2>/dev/null || true
docker compose up -d nacos-init
sleep 5
INIT_LOG=$(docker logs gov-nacos-init 2>&1 || true)
if echo "$INIT_LOG" | grep -q "Nacos 初始化完成"; then
  echo "  ✅ Nacos 初始化完成"
else
  echo "  ⚠️  Nacos 初始化可能未完成"
fi

echo ""
echo "========================================="
echo "  ✅ 数据库重建完成！"
echo "========================================="
echo ""
echo "📊 数据库验证："
docker exec gov-mysql mysql -uroot -proot123456 -N -e "
SELECT table_schema AS DB, COUNT(*) AS tables 
FROM information_schema.tables 
WHERE table_schema LIKE 'gov_%' 
GROUP BY table_schema 
ORDER BY table_schema;
" 2>/dev/null || true

echo ""
echo "� 声明 RabbitMQ 队列（容器重建后队列丢失）..."
for i in $(seq 1 10); do
  STATUS=$(curl -s -o /dev/null -w "%{http_code}" -u guest:guest \
    -X PUT "http://localhost:15673/api/queues/%2f/message.send" \
    -H "Content-Type: application/json" \
    -d '{"durable":true,"auto_delete":false}' 2>/dev/null)
  [ "$STATUS" = "201" ] || [ "$STATUS" = "204" ] && echo "✅ 队列 message.send 已就绪" && break
  [ "$i" -lt 10 ] && sleep 2
done
[ "$STATUS" != "201" ] && [ "$STATUS" != "204" ] && echo "⚠️  队列声明失败（message-service 启动后会自行声明）"

echo ""
echo "�📋 后续步骤："
echo "  1. 在 IDEA 启动网关 → gov-gateway（8091）"
echo "  2. 启动你负责的服务"
echo "  3. Knife4j: http://localhost:8091/doc.html"
echo ""
