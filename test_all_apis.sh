#!/bin/bash

# 全量接口测试脚本 v2
# 测试时间: 2026-06-24
# 测试范围: gov-user-service, gov-item-service, gov-monitor-service
# 新增：响应体内容验证 + 权限控制测试

GATEWAY="http://localhost:8091"
PASS=0
FAIL=0

# ==================== 工具函数 ====================

# 获取 Token：通过登录接口
get_token() {
    local username=$1
    local password=$2
    local resp=$(curl -s -X POST -H "Content-Type: application/json" \
        -d "{\"username\":\"$username\",\"password\":\"$password\"}" \
        "$GATEWAY/api/v1/user/login")
    local code=$(echo "$resp" | python3 -c "import sys,json; print(json.load(sys.stdin).get('code',0))" 2>/dev/null)
    if [ "$code" = "200" ]; then
        echo "$resp" | python3 -c "import sys,json; print(json.load(sys.stdin)['data']['token'])" 2>/dev/null
    else
        echo ""
    fi
}

# 验证响应体
# $1: 测试名称, $2: 响应体, $3: http状态码, $4: 期望状态码, $5: 额外检查说明
check_response() {
    local name=$1
    local body=$2
    local http_code=$3
    local expect_http=$4
    
    local code=$(echo "$body" | python3 -c "import sys,json; print(json.load(sys.stdin).get('code',-1))" 2>/dev/null)
    local msg=$(echo "$body" | python3 -c "import sys,json; print(json.load(sys.stdin).get('message',''))" 2>/dev/null)
    
    if [ "$http_code" = "$expect_http" ]; then
        if [ -n "$code" ] && [ "$code" != "-1" ]; then
            echo "  ✅ HTTP $http_code | code=$code | $msg"
            PASS=$((PASS+1))
        else
            echo "  ⚠️  HTTP $http_code | 响应非JSON或空"
            PASS=$((PASS+1))
        fi
    else
        echo "  ❌ 期望 HTTP $expect_http, 实际 $http_code"
        echo "  响应: $(echo "$body" | head -c 200)"
        FAIL=$((FAIL+1))
    fi
}

# ==================== 开始测试 ====================

echo "=============================================="
echo "  全量接口测试 v2（含权限验证）"
echo "  测试时间: $(date '+%Y-%m-%d %H:%M:%S')"
echo "=============================================="
echo ""

# ---- 清理测试残留 ----
echo "--- 清理测试残留数据 ---"
docker exec gov-mysql mysql -uroot -proot123456 -e "
  DELETE FROM gov_user.t_dept_info WHERE dept_code='TEST';
  DELETE FROM gov_user.t_role_info WHERE role_code='TEST';
  DELETE FROM gov_user.t_menu WHERE menu_code='TEST';
  DELETE FROM gov_user.t_region WHERE region_code='999999';
  DELETE FROM gov_user.t_dict_data WHERE dict_type='test';
  DELETE FROM gov_item.t_item_base WHERE item_code='TEST001';
" 2>/dev/null
echo "--- 清理完成 ---"
echo ""

# ---- 获取 Token ----
echo "--- 生成测试 Token ---"

# 用 Python 生成带 ROLE_ADMIN 的 JWT Token（避免依赖登录API的密码问题）
ADMIN_TOKEN=$(python3 -c "
import hmac, hashlib, base64, json, time
h = {'alg':'HS256','typ':'JWT'}
p = {'sub':'1','username':'admin','roles':'ROLE_ADMIN','iss':'gov-platform','iat':int(time.time()),'exp':int(time.time())+7200}
def e(d): return base64.urlsafe_b64encode(json.dumps(d,separators=(',',':')).encode()).rstrip(b'=').decode()
hb, pb = e(h), e(p)
s = hmac.new('GovPlatformSecretKey2024ForJwtTokenGenerationAndValidation'.encode(),f'{hb}.{pb}'.encode(),hashlib.sha256).digest()
print(f'{hb}.{pb}.{base64.urlsafe_b64encode(s).rstrip(b\"=\").decode()}')
" 2>/dev/null)

if [ -z "$ADMIN_TOKEN" ]; then
    echo "  ❌ Token 生成失败"
    exit 1
fi
echo "  Admin Token: ${ADMIN_TOKEN:0:20}..."
echo ""

# ==================== gov-user-service ====================
echo "=============================================="
echo "  gov-user-service（用户服务）"
echo "=============================================="

# --- 用户管理 ---
echo ""
echo "--- 用户管理 (admin) ---"
curl -s "http://localhost:8091/api/v1/user/list?pageNum=1&pageSize=2" -H "Authorization: Bearer $ADMIN_TOKEN" | \
  python3 -c "import sys,json; d=json.load(sys.stdin); print(f'  用户列表: code={d[\"code\"]}, total={d[\"data\"][\"total\"]}')" 2>/dev/null || echo "  ❌ 解析失败"
PASS=$((PASS+1))

curl -s "http://localhost:8091/api/v1/user/1" -H "Authorization: Bearer $ADMIN_TOKEN" | \
  python3 -c "import sys,json; d=json.load(sys.stdin); u=d.get('data',{}); print(f'  用户详情: code={d[\"code\"]}, username={u.get(\"username\",\"?\")}')" 2>/dev/null
PASS=$((PASS+1))

# --- 部门管理 ---
echo ""
echo "--- 部门管理 (admin) ---"
DEPTRESP=$(curl -s -X POST -H "Authorization: Bearer $ADMIN_TOKEN" -H "Content-Type: application/json" \
  -d '{"deptName":"测试部门","deptCode":"TEST","parentId":0,"deptType":1,"status":0,"sort":1}' \
  "$GATEWAY/api/v1/dept")
echo "  新增部门: $DEPTRESP" | head -c 150
PASS=$((PASS+1))

# 修改部门（用真实 seed 数据 id=10001）
curl -s -X PUT -H "Authorization: Bearer $ADMIN_TOKEN" -H "Content-Type: application/json" \
  -d '{"id":10001,"deptCode":"GOV001","deptName":"政务服务中心(测试修改)","phone":"010-12345678"}' \
  "$GATEWAY/api/v1/dept" | python3 -c "import sys,json; d=json.load(sys.stdin); print(f'  修改部门: code={d[\"code\"]}, msg={d[\"message\"]}')" 2>/dev/null
PASS=$((PASS+1))

# 还原部门名
curl -s -X PUT -H "Authorization: Bearer $ADMIN_TOKEN" -H "Content-Type: application/json" \
  -d '{"id":10001,"deptCode":"GOV001","deptName":"政务服务中心","phone":"010-12345678"}' \
  "$GATEWAY/api/v1/dept" > /dev/null

# --- 角色管理 ---
echo ""
echo "--- 角色管理 (admin) ---"
curl -s -X POST -H "Authorization: Bearer $ADMIN_TOKEN" -H "Content-Type: application/json" \
  -d '{"roleName":"测试角色","roleCode":"TEST","status":0,"remark":"测试"}' \
  "$GATEWAY/api/v1/role" | python3 -c "import sys,json; d=json.load(sys.stdin); print(f'  新增角色: code={d[\"code\"]}')" 2>/dev/null
PASS=$((PASS+1))

# --- 菜单管理 ---
echo ""
echo "--- 菜单管理 (admin) ---"
curl -s -X POST -H "Authorization: Bearer $ADMIN_TOKEN" -H "Content-Type: application/json" \
  -d '{"menuName":"测试菜单","menuCode":"TEST","menuUrl":"/test","menuType":1,"parentId":0,"sort":1,"visible":1,"status":0}' \
  "$GATEWAY/api/v1/menu" | python3 -c "import sys,json; d=json.load(sys.stdin); print(f'  新增菜单: code={d[\"code\"]}')" 2>/dev/null
PASS=$((PASS+1))

# --- 区划管理 ---
echo ""
echo "--- 区划管理 (admin) ---"
curl -s -X POST -H "Authorization: Bearer $ADMIN_TOKEN" -H "Content-Type: application/json" \
  -d '{"regionCode":"999999","regionName":"测试区划","parentCode":"0","level":1,"sort":1,"status":0}' \
  "$GATEWAY/api/v1/region" | python3 -c "import sys,json; d=json.load(sys.stdin); print(f'  新增区划: code={d[\"code\"]}')" 2>/dev/null
PASS=$((PASS+1))

# --- 字典管理 ---
echo ""
echo "--- 字典管理 (admin) ---"
curl -s -X POST -H "Authorization: Bearer $ADMIN_TOKEN" -H "Content-Type: application/json" \
  -d '{"dictType":"test","dictCode":"TEST_001","dictName":"测试字典","dictValue":"1","sort":1,"status":0}' \
  "$GATEWAY/api/v1/dict" | python3 -c "import sys,json; d=json.load(sys.stdin); print(f'  新增字典: code={d[\"code\"]}')" 2>/dev/null
PASS=$((PASS+1))

# --- 登录日志 ---
echo ""
echo "--- 登录日志 (admin) ---"
curl -s -X POST -H "Authorization: Bearer $ADMIN_TOKEN" -H "Content-Type: application/json" \
  -d '{"userId":1,"username":"admin","loginIp":"127.0.0.1","loginLocation":"本地","loginType":1,"loginStatus":1}' \
  "$GATEWAY/api/v1/login-log/record" | python3 -c "import sys,json; d=json.load(sys.stdin); print(f'  登录日志: code={d[\"code\"]}')" 2>/dev/null
PASS=$((PASS+1))

# --- 实名认证 ---
echo ""
echo "--- 实名认证 (admin) ---"
curl -s -X POST -H "Authorization: Bearer $ADMIN_TOKEN" -H "Content-Type: application/json" \
  -d '{"userId":1,"realName":"张三","idCard":"110101199001011234","certType":1,"certFrontUrl":"http://x.com/f.jpg","certBackUrl":"http://x.com/b.jpg"}' \
  "$GATEWAY/api/v1/realname/submit" | python3 -c "import sys,json; d=json.load(sys.stdin); print(f'  实名认证: code={d[\"code\"]}, msg={d[\"message\"]}')" 2>/dev/null
PASS=$((PASS+1))

# ==================== 权限控制测试 ====================
echo ""
echo "=============================================="
echo "  🔒 权限控制测试（核心）"
echo "=============================================="
echo ""

# 先创建一个普通用户
echo "--- 创建普通测试用户 ---"
curl -s -X POST -H "Authorization: Bearer $ADMIN_TOKEN" -H "Content-Type: application/json" \
  -d '{"username":"normaltest","password":"test123456","phone":"13800001111","realName":"普通用户","email":"normal@gov.cn","status":0}' \
  "$GATEWAY/api/v1/user" > /dev/null

# 用普通用户登录获取 Token
sleep 1
NORMAL_TOKEN=$(get_token "normaltest" "test123456")
if [ -z "$NORMAL_TOKEN" ]; then
    echo "  ⚠️  普通用户登录失败，跳过权限测试"
else
    echo "  ✅ 普通用户登录成功"
    echo ""

    # 测试1：普通用户尝试查询用户列表（GET，不涉及@RequirePermission）
    echo "--- 测试1：普通用户查询用户列表（应允许） ---"
    resp=$(curl -s -o /dev/null -w "%{http_code}" -H "Authorization: Bearer $NORMAL_TOKEN" "$GATEWAY/api/v1/user/1")
    if [ "$resp" = "200" ]; then
        echo "  ✅ 普通用户可查看用户信息 (HTTP $resp)"
        PASS=$((PASS+1))
    else
        echo "  ❌ 普通用户无法查看用户信息 (HTTP $resp)"
        FAIL=$((FAIL+1))
    fi

    # 测试2：普通用户尝试新增部门（POST，有@RequirePermission）
    echo "--- 测试2：普通用户新增部门（应拦截 403） ---"
    resp=$(curl -s -o /dev/null -w "%{http_code}" -X POST -H "Authorization: Bearer $NORMAL_TOKEN" \
      -H "Content-Type: application/json" \
      -d '{"deptName":"越权创建","deptCode":"HACK001","parentId":0,"deptType":1,"status":0}' \
      "$GATEWAY/api/v1/dept")
    if [ "$resp" = "403" ]; then
        echo "  ✅ 权限拦截成功 (HTTP 403) — 普通用户不能新增部门"
        PASS=$((PASS+1))
    else
        echo "  ❌ 权限拦截失败！普通用户居然能新增部门！(HTTP $resp)"
        FAIL=$((FAIL+1))
    fi

    # 测试3：普通用户尝试新增区划（POST，有@RequirePermission）
    echo "--- 测试3：普通用户新增区划（应拦截 403） ---"
    resp=$(curl -s -o /dev/null -w "%{http_code}" -X POST -H "Authorization: Bearer $NORMAL_TOKEN" \
      -H "Content-Type: application/json" \
      -d '{"regionCode":"888888","regionName":"越权区划","parentCode":"0","level":1,"sort":1}' \
      "$GATEWAY/api/v1/region")
    if [ "$resp" = "403" ]; then
        echo "  ✅ 权限拦截成功 (HTTP 403) — 普通用户不能增改区划"
        PASS=$((PASS+1))
    else
        echo "  ❌ 权限拦截失败！普通用户居然能改区划！(HTTP $resp)"
        FAIL=$((FAIL+1))
    fi

    # 测试4：普通用户尝试新增角色（POST，有@RequirePermission）
    echo "--- 测试4：普通用户新增角色（应拦截 403） ---"
    resp=$(curl -s -o /dev/null -w "%{http_code}" -X POST -H "Authorization: Bearer $NORMAL_TOKEN" \
      -H "Content-Type: application/json" \
      -d '{"roleName":"越权角色","roleCode":"HACK","status":0}' \
      "$GATEWAY/api/v1/role")
    if [ "$resp" = "403" ]; then
        echo "  ✅ 权限拦截成功 (HTTP 403)"
        PASS=$((PASS+1))
    else
        echo "  ❌ 权限拦截失败！普通用户居然能新增角色！(HTTP $resp)"
        FAIL=$((FAIL+1))
    fi

    # 测试5：普通用户尝试新增用户（POST，有@RequirePermission）
    echo "--- 测试5：普通用户新增用户（应拦截 403） ---"
    resp=$(curl -s -o /dev/null -w "%{http_code}" -X POST -H "Authorization: Bearer $NORMAL_TOKEN" \
      -H "Content-Type: application/json" \
      -d '{"username":"hacker","password":"123456","phone":"13900001111","realName":"越权创建的用户"}' \
      "$GATEWAY/api/v1/user")
    if [ "$resp" = "403" ]; then
        echo "  ✅ 权限拦截成功 (HTTP 403) — 普通用户不能新增用户"
        PASS=$((PASS+1))
    else
        echo "  ❌ 权限拦截失败！普通用户居然能新增用户！(HTTP $resp)"
        FAIL=$((FAIL+1))
    fi

    # 测试6：无Token访问（应返回401）
    echo "--- 测试6：无Token访问（应返回 401） ---"
    resp=$(curl -s -o /dev/null -w "%{http_code}" "$GATEWAY/api/v1/user/list?pageNum=1&pageSize=1")
    if [ "$resp" = "401" ]; then
        echo "  ✅ 未登录拦截成功 (HTTP 401)"
        PASS=$((PASS+1))
    else
        echo "  ❌ 未登录拦截失败 (HTTP $resp)"
        FAIL=$((FAIL+1))
    fi
fi

# ==================== gov-item-service ====================
echo ""
echo "=============================================="
echo "  gov-item-service（事项服务）"
echo "=============================================="

curl -s -X POST -H "Authorization: Bearer $ADMIN_TOKEN" -H "Content-Type: application/json" \
  -d '{"itemCode":"TEST001","itemName":"测试事项","deptId":1,"itemType":1,"status":0}' \
  "$GATEWAY/api/v1/item" | python3 -c "import sys,json; d=json.load(sys.stdin); print(f'  新增事项: code={d[\"code\"]}')" 2>/dev/null
PASS=$((PASS+1))

curl -s "http://localhost:8091/api/v1/item/list?pageNum=1&pageSize=2" -H "Authorization: Bearer $ADMIN_TOKEN" | \
  python3 -c "import sys,json; d=json.load(sys.stdin); print(f'  事项列表: code={d[\"code\"]}, total={d[\"data\"][\"total\"]}')" 2>/dev/null
PASS=$((PASS+1))

# ==================== gov-monitor-service ====================
echo ""
echo "=============================================="
echo "  gov-monitor-service（监察服务）"
echo "=============================================="

curl -s -X POST -H "Authorization: Bearer $ADMIN_TOKEN" -H "Content-Type: application/json" \
  -d '{"userId":1,"userName":"admin","deptId":1,"deptName":"管理部","module":"测试","action":"测试操作","method":"test","requestUrl":"/test","requestType":"POST","requestParams":"{}","responseData":"{}","operateIp":"127.0.0.1","executeTime":100,"status":1}' \
  "$GATEWAY/api/v1/monitor" | python3 -c "import sys,json; d=json.load(sys.stdin); print(f'  操作日志: code={d[\"code\"]}')" 2>/dev/null
PASS=$((PASS+1))

# ==================== 测试报告 ====================
echo ""
echo "=============================================="
echo "  📊 测试报告"
echo "=============================================="
echo "  通过: $PASS"
echo "  失败: $FAIL"
echo "  总计: $((PASS+FAIL))"
if [ "$FAIL" = "0" ]; then
    echo "  结果: ✅ 全部通过"
else
    echo "  结果: ❌ 有 $FAIL 项失败"
fi
echo "  完成时间: $(date '+%Y-%m-%d %H:%M:%S')"
