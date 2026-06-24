#!/bin/bash
# =====================================================
# Nacos 自动初始化脚本
# 用途：自动创建命名空间 + 导入配置 + 导入 Sentinel 规则
# 被 docker-compose 中的 nacos-init 容器调用
# =====================================================

set -e

NACOS_URL="${NACOS_URL:-http://nacos:8848}"
NACOS_USERNAME="${NACOS_USERNAME:-nacos}"
NACOS_PASSWORD="${NACOS_PASSWORD:-nacos}"
NAMESPACE_ID="gov-platform"
NAMESPACE_NAME="智慧政务平台"

echo "========================================="
echo "  Nacos 自动初始化脚本"
echo "  Nacos 地址: ${NACOS_URL}"
echo "========================================="

# -------- 1. 等待 Nacos 就绪 --------
echo "[1/5] 等待 Nacos 启动..."
MAX_RETRIES=60
RETRY_COUNT=0
until curl -s "${NACOS_URL}/nacos/v1/console/health/readiness" | grep -q "ok"; do
  RETRY_COUNT=$((RETRY_COUNT + 1))
  if [ $RETRY_COUNT -ge $MAX_RETRIES ]; then
    echo "ERROR: Nacos 启动超时，请检查 Nacos 容器日志"
    exit 1
  fi
  echo "  等待中... (${RETRY_COUNT}/${MAX_RETRIES})"
  sleep 3
done
echo "  Nacos 已就绪!"

# -------- 2. 登录获取 Token --------
echo "[2/5] 登录 Nacos..."
LOGIN_RESPONSE=$(curl -s -X POST "${NACOS_URL}/nacos/v1/auth/login" \
  -d "username=${NACOS_USERNAME}&password=${NACOS_PASSWORD}")
ACCESS_TOKEN=$(echo "$LOGIN_RESPONSE" | grep -o '"accessToken":"[^"]*"' | cut -d'"' -f4)

if [ -z "$ACCESS_TOKEN" ]; then
  echo "ERROR: 登录失败，响应: ${LOGIN_RESPONSE}"
  exit 1
fi
echo "  登录成功，Token: ${ACCESS_TOKEN:0:20}..."

# -------- 3. 创建命名空间 --------
echo "[3/5] 创建命名空间 ${NAMESPACE_ID}..."
NS_RESPONSE=$(curl -s -X POST "${NACOS_URL}/nacos/v1/console/namespaces" \
  -d "accessToken=${ACCESS_TOKEN}" \
  -d "customNamespaceId=${NAMESPACE_ID}" \
  -d "namespaceName=${NAMESPACE_NAME}" \
  -d "namespaceDesc=智慧政务一体化便民服务平台开发环境")

if echo "$NS_RESPONSE" | grep -q '"data":true'; then
  echo "  命名空间 ${NAMESPACE_ID} 创建成功"
elif echo "$NS_RESPONSE" | grep -q '"message":"已存在"'; then
  echo "  命名空间 ${NAMESPACE_ID} 已存在，跳过创建"
else
  echo "  创建结果: ${NS_RESPONSE}"
fi

# -------- 4. 导入 gov-platform-common.yaml 配置 --------
echo "[4/5] 导入共享配置 gov-platform-common.yaml..."
CONFIG_FILE="/nacos-config/gov-platform-common.yaml"
if [ -f "$CONFIG_FILE" ]; then
  CONFIG_CONTENT=$(cat "$CONFIG_FILE")
  IMPORT_RESPONSE=$(curl -s -X POST "${NACOS_URL}/nacos/v1/cs/configs" \
    -d "accessToken=${ACCESS_TOKEN}" \
    -d "tenant=${NAMESPACE_ID}" \
    -d "dataId=gov-platform-common.yaml" \
    -d "group=DEFAULT_GROUP" \
    -d "type=yaml" \
    -d "content=${CONFIG_CONTENT}")
  if [ "$IMPORT_RESPONSE" = "true" ]; then
    echo "  gov-platform-common.yaml 导入成功"
  else
    echo "  导入结果: ${IMPORT_RESPONSE}"
  fi
else
  echo "  WARNING: 配置文件 ${CONFIG_FILE} 不存在，跳过"
fi

# -------- 5. 导入 Sentinel 流控规则 --------
echo "[5/5] 导入 Sentinel 流控规则 gov-sentinel-flow-rules.json..."
RULES_FILE="/nacos-config/gov-sentinel-flow-rules.json"
if [ -f "$RULES_FILE" ]; then
  RULES_CONTENT=$(cat "$RULES_FILE")
  RULES_RESPONSE=$(curl -s -X POST "${NACOS_URL}/nacos/v1/cs/configs" \
    -d "accessToken=${ACCESS_TOKEN}" \
    -d "tenant=${NAMESPACE_ID}" \
    -d "dataId=gov-sentinel-flow-rules.json" \
    -d "group=DEFAULT_GROUP" \
    -d "type=json" \
    -d "content=${RULES_CONTENT}")
  if [ "$RULES_RESPONSE" = "true" ]; then
    echo "  gov-sentinel-flow-rules.json 导入成功"
  else
    echo "  导入结果: ${RULES_RESPONSE}"
  fi
else
  echo "  WARNING: 规则文件 ${RULES_FILE} 不存在，跳过"
fi

echo ""
echo "========================================="
echo "  Nacos 初始化完成!"
echo "  验证: ${NACOS_URL}/nacos"
echo "========================================="
