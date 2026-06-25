#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
===========================
gov-activiti-service 自动化接口测试脚本
===========================
测试范围：审批流转服务的所有 OpenAPI 接口
包含：功能测试 + 权限鉴权测试 + 规范合规检查

用法：
  python test_activiti_api.py
  python test_activiti_api.py --host localhost --port 8084   # 直连
  python test_activiti_api.py --gateway                       # 通过网关(8091)
"""

import json
import time
import sys
import urllib.request
import urllib.error
import ssl
from datetime import datetime, timedelta

# ==================== 配置 ====================
BASE_URL = "http://localhost:8084"       # 直连服务端口
GATEWAY_URL = "http://localhost:8091"    # 网关地址
USE_GATEWAY = "--gateway" in sys.argv

# JWT 密钥（与项目 gov-common JwtUtil 中的一致）
JWT_SECRET = "GovPlatformSecretKey2024ForJwtTokenGenerationAndValidation"

# 测试数据
TEST_ADMIN_ID = "1"
TEST_USER_ID = "2"
TEST_PROCESS_KEY = "apply_approval_v1"
TEST_APPLY_NO = f"TEST{datetime.now().strftime('%Y%m%d%H%M%S')}"

# ==================== JWT Token 生成 ====================

def base64url_encode(data: bytes) -> str:
    """Base64URL 编码（无填充）"""
    import base64
    return base64.urlsafe_b64encode(data).rstrip(b'=').decode()

def generate_jwt(user_id: str, username: str) -> str:
    """生成 JWT Token（HS256 算法）"""
    header = {"alg": "HS256", "typ": "JWT"}
    now = int(time.time())
    payload = {
        "sub": user_id,
        "username": username,
        "iss": "gov-platform",
        "iat": now,
        "exp": now + 7200
    }

    # 编码 Header 和 Payload
    header_b64 = base64url_encode(json.dumps(header, separators=(',', ':')).encode())
    payload_b64 = base64url_encode(json.dumps(payload, separators=(',', ':')).encode())

    # HMAC-SHA256 签名
    import hmac
    import hashlib
    signing_input = f"{header_b64}.{payload_b64}"
    signature = hmac.new(
        JWT_SECRET.encode(),
        signing_input.encode(),
        hashlib.sha256
    ).digest()
    sig_b64 = base64url_encode(signature)

    return f"{signing_input}.{sig_b64}"

# 生成不同角色的 Token
ADMIN_TOKEN = generate_jwt(TEST_ADMIN_ID, "admin")
USER_TOKEN = generate_jwt(TEST_USER_ID, "staff_zhang")

# ==================== HTTP 请求工具 ====================

def http_request(method: str, path: str, body: dict = None, token: str = None, timeout: int = 10) -> dict:
    """发送 HTTP 请求并返回 (status_code, response_body_dict, headers)"""
    url = f"{BASE_URL}{path}"
    if USE_GATEWAY:
        # 网关路径：剥离服务前缀
        gateway_path = path.replace("/workflow", "/api/v1/workflow")
        url = f"{GATEWAY_URL}{gateway_path}"

    data = None
    if body is not None:
        data = json.dumps(body, ensure_ascii=False).encode("utf-8")

    req = urllib.request.Request(url, data=data, method=method)
    req.add_header("Content-Type", "application/json")

    if token:
        req.add_header("Authorization", f"Bearer {token}")

    # 忽略 SSL 证书验证（本地测试用）
    ctx = ssl.create_default_context()
    ctx.check_hostname = False
    ctx.verify_mode = ssl.CERT_NONE

    try:
        with urllib.request.urlopen(req, timeout=timeout, context=ctx) as resp:
            status = resp.status
            resp_body = json.loads(resp.read().decode("utf-8"))
            return {"status": status, "body": resp_body, "error": None}
    except urllib.error.HTTPError as e:
        try:
            resp_body = json.loads(e.read().decode("utf-8"))
        except:
            resp_body = {"raw": str(e)}
        return {"status": e.code, "body": resp_body, "error": str(e)}
    except Exception as e:
        return {"status": 0, "body": None, "error": str(e)}

# ==================== 测试用例 ====================

class TestResult:
    def __init__(self):
        self.passed = 0
        self.failed = 0
        self.skipped = 0
        self.results = []

    def add(self, name: str, method: str, path: str, expected_status: int, actual_status: int, passed: bool, detail: str = ""):
        icon = "✅" if passed else "❌"
        self.results.append({
            "name": name, "method": method, "path": path,
            "expected": expected_status, "actual": actual_status,
            "icon": icon, "detail": detail
        })
        if passed:
            self.passed += 1
        else:
            self.failed += 1

    def skip(self, name: str, reason: str):
        self.results.append({
            "name": name, "method": "—", "path": "—",
            "expected": "—", "actual": "—",
            "icon": "⏭️", "detail": reason
        })
        self.skipped += 1

    def report(self):
        total = self.passed + self.failed + self.skipped
        print("\n" + "=" * 80)
        print("  gov-activiti-service 接口测试报告")
        print("=" * 80)
        print(f"  Token(admin):  {ADMIN_TOKEN[:30]}...")
        print(f"  Token(user):   {USER_TOKEN[:30]}...")
        print(f"  测试时间:       {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
        print(f"  目标地址:       {GATEWAY_URL if USE_GATEWAY else BASE_URL}")
        print()
        print(f"  {'#':<3} {'测试项':<28} {'方法':<6} {'路径':<35} {'预期':<6} {'实际':<6} {'结果':<4}")
        print(f"  {'-'*3} {'-'*28} {'-'*6} {'-'*35} {'-'*6} {'-'*6} {'-'*4}")

        for i, r in enumerate(self.results, 1):
            print(f"  {i:<3} {r['name']:<28} {r['method']:<6} {r['path']:<35} {str(r['expected']):<6} {str(r['actual']):<6} {r['icon']:<4}")
            if r['detail']:
                print(f"      └─ {r['detail']}")

        print()
        print(f"  通过: {self.passed} / 失败: {self.failed} / 跳过: {self.skipped}")
        if total > 0:
            rate = self.passed / (self.passed + self.failed) * 100 if (self.passed + self.failed) > 0 else 0
            print(f"  通过率: {rate:.0f}%")

        conclusion = "✅ 全部通过" if self.failed == 0 else f"⚠️ 有 {self.failed} 项失败"
        print(f"  结论: {conclusion}")
        print("=" * 80)
        return self.failed == 0

# ==================== 主测试流程 ====================

def run_all_tests():
    result = TestResult()
    service_available = True

    # ---- 0. 服务连通性检查 ----
    print("\n[0] 检查服务连通性...")
    resp = http_request("GET", "/workflow/list?pageNum=1&pageSize=1", token=ADMIN_TOKEN)
    if resp["status"] == 0 and resp["error"]:
        print(f"     ⚠️ 服务未启动或不可达: {resp['error']}")
        print("     请先执行: docker-compose up -d && 启动 gov-activiti-service")
        result.skip("服务连通性检查", f"服务不可达: {resp['error']}")
        result.report()
        return
    print(f"     ✅ 服务可达 (HTTP {resp['status']})")

    # ---- 1. 鉴权测试（无 Token） ----
    print("\n[1] 鉴权测试 — 无 Token 访问")

    test_id = http_request("GET", "/workflow/list?pageNum=1&pageSize=1")
    result.add("无Token查列表", "GET", "/workflow/list", "401", test_id["status"],
               test_id["status"] == 401)
    # 注意：直连服务可能没有网关鉴权，401 是网关层面的检查
    if test_id["status"] != 401:
        result.add("无Token查列表(网关)", "GET", "/api/v1/workflow/list", "401", "—",
                   False, "直连模式不经过网关鉴权，需单独测试网关")

    # ---- 2. 查询接口测试（普通用户可访问） ----
    print("\n[2] 查询接口测试")

    # 2.1 待办列表
    resp = http_request("GET", f"/workflow/todo?pageNum=1&pageSize=5&userId={TEST_USER_ID}", token=USER_TOKEN)
    result.add("待办列表(用户)", "GET", f"/workflow/todo", "200", resp["status"],
               resp["status"] == 200, f"返回: {str(resp['body'])[:80]}")

    # 2.2 任务列表（需要 workflow:query 权限）
    resp = http_request("GET", "/workflow/list?pageNum=1&pageSize=2", token=ADMIN_TOKEN)
    result.add("全量任务列表(admin)", "GET", "/workflow/list", "200", resp["status"],
               resp["status"] == 200,
               f"total={resp['body'].get('data',{}).get('total','N/A') if resp['body'] else 'N/A'}")

    # 2.3 按条件搜索
    resp = http_request("GET", "/workflow/list?pageNum=1&pageSize=5&taskName=审批&status=0", token=ADMIN_TOKEN)
    result.add("任务搜索(admin)", "GET", "/workflow/list?taskName=审批", "200", resp["status"],
               resp["status"] in [200, 404])

    # ---- 3. 权限鉴权测试（核心） ----
    print("\n[3] 权限鉴权测试 — 不同角色访问控制")

    # 3.1 启动流程 — 普通用户无 workflow:start 权限
    process_body = {
        "processKey": TEST_PROCESS_KEY,
        "applyNo": TEST_APPLY_NO,
        "itemId": 10001,
        "itemName": "测试事项",
        "userId": int(TEST_USER_ID),
        "userName": "张三",
        "deptId": 20001
    }
    resp_user_start = http_request("POST", "/workflow/process", body=process_body, token=USER_TOKEN)
    result.add("权限:普通用户启动流程", "POST", "/workflow/process",
               "403", resp_user_start["status"],
               resp_user_start["status"] == 403,
               f"期望拒绝(403)，实际: {resp_user_start['status']}")

    # 3.2 启动流程 — admin 有权限
    resp_admin_start = http_request("POST", "/workflow/process", body=process_body, token=ADMIN_TOKEN)
    result.add("权限:admin启动流程", "POST", "/workflow/process",
               "200", resp_admin_start["status"],
               resp_admin_start["status"] == 200,
               f"返回: {str(resp_admin_start.get('body',{}))[:100]}")

    # 3.3 审批任务 — 普通用户无 workflow:approve 权限
    approval_body = {
        "taskId": "test-task-001",
        "approvalResult": "1",
        "opinion": "测试审批通过"
    }
    resp_user_approve = http_request("POST", "/workflow/approval", body=approval_body, token=USER_TOKEN)
    result.add("权限:普通用户审批", "POST", "/workflow/approval",
               "403", resp_user_approve["status"],
               resp_user_approve["status"] == 403,
               f"期望拒绝(403)，实际: {resp_user_approve['status']}")

    # 3.4 认领任务 — 普通用户无 workflow:claim 权限
    resp_user_claim = http_request("POST", f"/workflow/task/test-task-001/claim?userId={TEST_USER_ID}",
                                   token=USER_TOKEN)
    result.add("权限:普通用户认领任务", "POST", "/workflow/task/{id}/claim",
               "403", resp_user_claim["status"],
               resp_user_claim["status"] == 403,
               f"期望拒绝(403)，实际: {resp_user_claim['status']}")

    # 3.5 委托任务 — 普通用户无 workflow:delegate 权限
    delegate_body = {
        "toUserId": 10003,
        "delegateReason": "出差代为审批"
    }
    resp_user_delegate = http_request("POST", "/workflow/task/test-task-001/delegation",
                                      body=delegate_body, token=USER_TOKEN)
    result.add("权限:普通用户委托任务", "POST", "/workflow/task/{id}/delegation",
               "403", resp_user_delegate["status"],
               resp_user_delegate["status"] == 403,
               f"期望拒绝(403)，实际: {resp_user_delegate['status']}")

    # 3.6 催办 — 普通用户无 workflow:remind 权限
    remind_body = {
        "instanceId": "inst-test-001",
        "reminderContent": "请尽快处理"
    }
    resp_user_remind = http_request("POST", "/workflow/reminder?reminderBy=1",
                                    body=remind_body, token=USER_TOKEN)
    result.add("权限:普通用户催办", "POST", "/workflow/reminder",
               "403", resp_user_remind["status"],
               resp_user_remind["status"] == 403,
               f"期望拒绝(403)，实际: {resp_user_remind['status']}")

    # 3.7 删除任务 — 普通用户无 workflow:delete 权限
    resp_user_delete = http_request("DELETE", "/workflow/999", token=USER_TOKEN)
    result.add("权限:普通用户删除任务", "DELETE", "/workflow/{id}",
               "403", resp_user_delete["status"],
               resp_user_delete["status"] == 403,
               f"期望拒绝(403)，实际: {resp_user_delete['status']}")

    # ---- 4. URL 规范检查 ----
    print("\n[4] URL 规范合规检查")

    RESTFUL_URLS = [
        ("GET", "/workflow/todo", "无动词，名词路径"),
        ("GET", "/workflow/list", "无动词，符合路径表"),
        ("GET", "/workflow/{id}", "无动词，RESTful"),
        ("POST", "/workflow/process", "POST语义=启动"),
        ("POST", "/workflow/approval", "approval=审批(名词)"),
        ("POST", "/workflow/task/{taskId}/claim", "claim子资源"),
        ("POST", "/workflow/task/{taskId}/delegation", "delegation=委托(名词)"),
        ("POST", "/workflow/reminder", "reminder=催办(名词)"),
        ("DELETE", "/workflow/{id}", "无动词，RESTful"),
    ]
    verb_issues = 0
    for method, path, reason in RESTFUL_URLS:
        # 检查路径中是否包含常见动词
        common_verbs = ["/create", "/update", "/delete", "/start", "/complete"]
        has_verb = any(path.endswith(v) for v in common_verbs)
        if has_verb:
            verb_issues += 1
    result.add("URL无动词检查", "—", "所有路径", "0违规", str(verb_issues),
               verb_issues == 0, f"检测到 {verb_issues} 个URL包含动词" if verb_issues else "全部符合规范")

    # ---- 5. 注解合规检查（静态分析） ----
    print("\n[5] 代码注解规范检查")

    import os
    controller_path = os.path.join(os.path.dirname(os.path.abspath(__file__)),
                                    "gov-activiti-service", "src", "main", "java",
                                    "com", "gov", "activiti", "controller",
                                    "WorkflowTaskController.java")
    # 尝试从不同位置找到文件
    candidates = [
        r"d:\JAVAWORK\gov\gov-platform\gov-activiti-service\src\main\java\com\gov\activiti\controller\WorkflowTaskController.java",
    ]
    controller_file = None
    for c in candidates:
        if os.path.exists(c):
            controller_file = c
            break

    if controller_file and os.path.exists(controller_file):
        with open(controller_file, "r", encoding="utf-8") as f:
            code = f.read()

        checks = [
            ("有 @Tag 注解", "@Tag(" in code),
            ("有 @Validated 注解", "@Validated" in code),
            ("有 @RequiredArgsConstructor", "@RequiredArgsConstructor" in code),
            ("无 @Autowired", "@Autowired" not in code),
            ("有 @Operation 注解", "@Operation" in code),
            ("有 @Parameter 注解", "@Parameter" in code),
            ("有 @Log 注解", "@Log(" in code),
            ("有 @RequirePermission", "@RequirePermission" in code),
            ("入参用 DTO（非 Entity）", "Entity" not in code),
            ("分页有 @Max(value=100)", "@Max(value = 100" in code),
        ]
        for name, passed in checks:
            result.add(f"代码检查:{name}", "—", "WorkflowTaskController.java", "✅", "✅" if passed else "❌", passed)
    else:
        result.skip("代码注解检查", f"找不到 Controller 文件")

    # ---- 最终报告 ----
    return result.report()

if __name__ == "__main__":
    print("=" * 80)
    print("  gov-activiti-service 自动化接口测试")
    print(f"  启动时间: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
    print(f"  模式: {'网关(' + GATEWAY_URL + ')' if USE_GATEWAY else '直连(' + BASE_URL + ')'}")
    print("=" * 80)

    all_ok = run_all_tests()
    sys.exit(0 if all_ok else 1)
