#!/bin/bash

# 全量接口测试脚本
# 测试时间: 2026-06-24
# 测试范围: gov-user-service, gov-item-service, gov-monitor-service

TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwidXNlcm5hbWUiOiJhZG1pbiIsImlzcyI6Imdvdi1wbGF0Zm9ybSIsImlhdCI6MTc4MjMwNzQ4OSwiZXhwIjoxNzgyMzE0Njg5fQ.0XvWX6YouIiZf4PnzCkRDPKljVcGeVSsco7hTrl3c-k"
GATEWAY="http://localhost:8091"

echo "=== 开始全量接口测试 ==="
echo "测试时间: $(date '+%Y-%m-%d %H:%M:%S')"
echo "Token: ${TOKEN:0:20}..."
echo ""

# 前置清理：删除上次测试残留的软删除数据（唯一索引不含deleted，需硬删）
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

# 测试函数
test_api() {
    local name=$1
    local method=$2
    local url=$3
    local data=$4
    local expect_code=$5
    
    echo -n "测试: $name - $method $url ... "
    
    if [ "$method" = "GET" ]; then
        response=$(curl -s -w "\n%{http_code}" -H "Authorization: Bearer $TOKEN" "$GATEWAY$url")
    elif [ "$method" = "POST" ]; then
        response=$(curl -s -w "\n%{http_code}" -X POST -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d "$data" "$GATEWAY$url")
    elif [ "$method" = "PUT" ]; then
        response=$(curl -s -w "\n%{http_code}" -X PUT -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" -d "$data" "$GATEWAY$url")
    elif [ "$method" = "DELETE" ]; then
        response=$(curl -s -w "\n%{http_code}" -X DELETE -H "Authorization: Bearer $TOKEN" "$GATEWAY$url")
    fi
    
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | sed '$d')
    
    if [ "$http_code" = "$expect_code" ]; then
        echo "✅ 通过 (HTTP $http_code)"
    else
        echo "❌ 失败 (期望 $expect_code, 实际 $http_code)"
        echo "   响应: $body"
    fi
}

# 测试无 Token 鉴权
test_no_token() {
    local name=$1
    local url=$2
    
    echo -n "测试: $name - 无Token鉴权 ... "
    http_code=$(curl -s -o /dev/null -w "%{http_code}" "$GATEWAY$url")
    
    if [ "$http_code" = "401" ]; then
        echo "✅ 通过 (HTTP 401)"
    else
        echo "❌ 失败 (期望 401, 实际 $http_code)"
    fi
}

echo "==================== gov-user-service (用户服务) ===================="

# 用户管理
test_api "用户列表分页" "GET" "/api/v1/user/list?pageNum=1&pageSize=2" "" "200"
test_api "用户列表搜索" "GET" "/api/v1/user/list?pageNum=1&pageSize=5&username=admin" "" "200"
test_api "根据ID查询用户" "GET" "/api/v1/user/1" "" "200"
test_api "根据用户名查询" "GET" "/api/v1/user/byUsername?username=admin" "" "200"
test_api "新增用户" "POST" "/api/v1/user" '{"username":"testuser","password":"123456","phone":"13800138000","realName":"测试用户","email":"test@gov.cn","status":0}' "200"
test_api "修改用户" "PUT" "/api/v1/user" '{"id":2,"username":"testuser","realName":"更新用户","email":"updated@gov.cn"}' "200"
test_api "删除用户" "DELETE" "/api/v1/user/2" "" "200"
test_no_token "用户列表无Token" "/api/v1/user/list?pageNum=1&pageSize=1"

# 部门管理
test_api "部门列表" "GET" "/api/v1/dept/list" "" "200"
test_api "部门分页" "GET" "/api/v1/dept/page?pageNum=1&pageSize=2" "" "200"
test_api "根据ID查询部门" "GET" "/api/v1/dept/1" "" "200"
test_api "根据编码查询部门" "GET" "/api/v1/dept/byCode?deptCode=GOV" "" "200"
test_api "新增部门" "POST" "/api/v1/dept" '{"deptName":"测试部门","deptCode":"TEST","parentId":0,"deptType":1,"status":0,"sort":1}' "200"
test_api "修改部门" "PUT" "/api/v1/dept" '{"id":10001,"deptCode":"GOV001","deptName":"政务服务中心（测试更新）","phone":"010-12345678"}' "200"
test_api "删除部门" "DELETE" "/api/v1/dept/2" "" "200"
test_no_token "部门列表无Token" "/api/v1/dept/list"

# 角色管理
test_api "角色列表" "GET" "/api/v1/role/list" "" "200"
test_api "角色分页" "GET" "/api/v1/role/page?pageNum=1&pageSize=2" "" "200"
test_api "根据ID查询角色" "GET" "/api/v1/role/1" "" "200"
test_api "根据编码查询角色" "GET" "/api/v1/role/byCode?roleCode=ADMIN" "" "200"
test_api "查询用户角色" "GET" "/api/v1/role/user/1" "" "200"
test_api "新增角色" "POST" "/api/v1/role" '{"roleName":"测试角色","roleCode":"TEST","status":0,"remark":"测试角色"}' "200"
test_api "修改角色" "PUT" "/api/v1/role" '{"id":1,"roleCode":"admin","roleName":"超级管理员（测试更新）","remark":"测试更新"}' "200"
test_api "删除角色" "DELETE" "/api/v1/role/2" "" "200"
test_no_token "角色列表无Token" "/api/v1/role/list"

# 菜单管理
test_api "菜单列表" "GET" "/api/v1/menu/list" "" "200"
test_api "根据ID查询菜单" "GET" "/api/v1/menu/1" "" "200"
test_api "查询用户菜单" "GET" "/api/v1/menu/user/1" "" "200"
test_api "查询角色菜单" "GET" "/api/v1/menu/role/1" "" "200"
test_api "新增菜单" "POST" "/api/v1/menu" '{"menuName":"测试菜单","menuCode":"TEST","menuUrl":"/test","menuType":1,"parentId":0,"sort":1,"visible":1,"status":0}' "200"
test_api "修改菜单" "PUT" "/api/v1/menu" '{"id":1,"menuCode":"root","menuName":"政务服务平台（测试更新）","icon":"setting"}' "200"
test_api "删除菜单" "DELETE" "/api/v1/menu/2" "" "200"
test_no_token "菜单列表无Token" "/api/v1/menu/list"

# 字典管理
test_api "根据类型查询字典" "GET" "/api/v1/dict/type/gender" "" "200"
test_api "字典分页" "GET" "/api/v1/dict/page?pageNum=1&pageSize=2" "" "200"
test_api "根据ID查询字典" "GET" "/api/v1/dict/1" "" "200"
test_api "新增字典" "POST" "/api/v1/dict" '{"dictType":"test","dictCode":"TEST_001","dictName":"测试字典","dictValue":"1","sort":1,"status":0}' "200"
test_api "修改字典" "PUT" "/api/v1/dict" '{"id":2,"dictType":"test","dictCode":"TEST_001","dictName":"更新字典","dictValue":"2","sort":1,"remark":"已更新"}' "200"
test_api "删除字典" "DELETE" "/api/v1/dict/2" "" "200"
test_no_token "字典查询无Token" "/api/v1/dict/type/gender"

# 区划管理
test_api "区划列表" "GET" "/api/v1/region/list" "" "200"
test_api "根据父级查询子区划" "GET" "/api/v1/region/children?parentCode=110000" "" "200"
test_api "根据ID查询区划" "GET" "/api/v1/region/1" "" "200"
test_api "根据编码查询区划" "GET" "/api/v1/region/byCode?regionCode=110000" "" "200"
test_api "新增区划" "POST" "/api/v1/region" '{"regionCode":"999999","regionName":"测试区划","parentCode":"0","level":1,"sort":1,"status":0}' "200"
test_api "修改区划" "PUT" "/api/v1/region" '{"id":2,"regionCode":"320500","regionName":"苏州市（测试更新）","parentCode":"320000","level":2,"sort":1}' "200"
test_api "删除区划" "DELETE" "/api/v1/region/2" "" "200"
test_no_token "区划列表无Token" "/api/v1/region/list"

# 登录日志
test_api "登录日志分页" "GET" "/api/v1/login-log/page?pageNum=1&pageSize=2" "" "200"
test_api "根据ID查询日志" "GET" "/api/v1/login-log/1" "" "200"
test_api "记录登录日志" "POST" "/api/v1/login-log/record" '{"userId":1,"username":"admin","loginIp":"127.0.0.1","loginLocation":"本地","browser":"Chrome","os":"MacOS","status":0}' "200"
test_no_token "登录日志无Token" "/api/v1/login-log/page?pageNum=1&pageSize=1"

# 实名认证
test_api "查询实名认证" "GET" "/api/v1/realname/user/1" "" "200"
test_api "实名认证分页" "GET" "/api/v1/realname/page?pageNum=1&pageSize=2" "" "200"
test_api "提交实名认证" "POST" "/api/v1/realname/submit" '{"userId":1,"realName":"张三","idCard":"110101199001011234","idCardFront":"http://example.com/front.jpg","idCardBack":"http://example.com/back.jpg"}' "200"
test_no_token "实名认证无Token" "/api/v1/realname/user/1"

echo ""
echo "==================== gov-item-service (事项服务) ===================="

# 事项管理
test_api "事项列表分页" "GET" "/api/v1/item/list?pageNum=1&pageSize=2" "" "200"
test_api "事项列表搜索" "GET" "/api/v1/item/list?pageNum=1&pageSize=5&itemName=item" "" "200"
test_api "根据ID查询事项" "GET" "/api/v1/item/1" "" "200"
test_api "根据编码查询事项" "GET" "/api/v1/item/byCode?itemCode=ITEM001" "" "200"
test_api "新增事项" "POST" "/api/v1/item" '{"itemCode":"TEST001","itemName":"测试事项","deptId":1,"itemType":1,"status":0}' "200"
test_api "修改事项" "PUT" "/api/v1/item" '{"id":2,"itemCode":"TEST001","itemName":"更新事项","status":1}' "200"
test_api "删除事项" "DELETE" "/api/v1/item/2" "" "200"
test_no_token "事项列表无Token" "/api/v1/item/list?pageNum=1&pageSize=1"

echo ""
echo "==================== gov-monitor-service (监控服务) ===================="

# 操作日志
test_api "操作日志分页" "GET" "/api/v1/monitor/list?pageNum=1&pageSize=2" "" "200"
test_api "根据ID查询日志" "GET" "/api/v1/monitor/1" "" "200"
test_api "新增操作日志" "POST" "/api/v1/monitor" '{"userId":1,"userName":"admin","deptId":1,"deptName":"管理部","module":"测试模块","action":"测试操作","method":"com.gov.test.Test.method","requestUrl":"/test","requestType":"POST","requestParams":"{}","responseData":"{}","operateIp":"127.0.0.1","executeTime":100,"status":1}' "200"
test_api "删除操作日志" "DELETE" "/api/v1/monitor/2" "" "200"
test_no_token "操作日志无Token" "/api/v1/monitor/list?pageNum=1&pageSize=1"

echo ""
echo "=== 测试完成 ==="
echo "测试时间: $(date '+%Y-%m-%d %H:%M:%S')"
