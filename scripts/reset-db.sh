#!/bin/bash
# ============================================
# 数据库重置脚本 — 队员人手一份
# 用途: 重建所有数据库并加载最新建表+种子数据
# 用法: bash scripts/reset-db.sh
# 注意: 请在项目根目录下执行
# ============================================
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
cd "$PROJECT_DIR/docker"

echo "=== 停止并删除旧容器 + 数据卷 ==="
docker compose -f docker-compose.yml down -v 2>/dev/null || true

echo "=== 重建 MySQL + Redis + Nacos ==="
docker compose -f docker-compose.yml up -d

echo "=== 等待 MySQL 就绪（最多30秒）==="
for i in $(seq 1 30); do
  if mysql -h 127.0.0.1 -P 3307 -u root -proot123456 -e "SELECT 1" 2>/dev/null; then
    echo "✅ MySQL 就绪（${i}秒）"
    break
  fi
  sleep 1
done

# MySQL 容器启动时会自动按序号执行 init/*.sql
echo ""
echo "=== 验证数据库 ==="
mysql -h 127.0.0.1 -P 3307 -u root -proot123456 -N -e "
SELECT table_schema AS DB, COUNT(*) AS tables 
FROM information_schema.tables 
WHERE table_schema LIKE 'gov_%' 
GROUP BY table_schema 
ORDER BY table_schema;
" 2>/dev/null

echo ""
echo "=== 验证种子数据 ==="
mysql -h 127.0.0.1 -P 3307 -u root -proot123456 -N -e "
SELECT 'gov_user' AS DB, COUNT(*) FROM gov_user.t_user_info UNION
SELECT 'gov_item', COUNT(*) FROM gov_item.t_item_base UNION
SELECT 'gov_reception', COUNT(*) FROM gov_reception.t_reception_record UNION
SELECT 'gov_activiti', COUNT(*) FROM gov_activiti.t_workflow_opinion UNION
SELECT 'gov_license', COUNT(*) FROM gov_license.t_license_data UNION
SELECT 'gov_complaint', COUNT(*) FROM gov_complaint.t_complaint_work UNION
SELECT 'gov_open', COUNT(*) FROM gov_open.t_open_notice UNION
SELECT 'gov_message', COUNT(*) FROM gov_message.t_message_record UNION
SELECT 'gov_monitor', COUNT(*) FROM gov_monitor.t_operate_log UNION
SELECT 'gov_datashare', COUNT(*) FROM gov_datashare.t_share_log;
" 2>/dev/null

echo ""
echo "=== ✅ 数据库重置完成 ==="
echo "下一步: 启动网关 → 启动自己负责的服务"
