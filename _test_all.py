#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
全量 API 冒烟测试（通过网关）
测试所有服务的基础连通性 + 鉴权
用法：python3 _test_all.py
"""
import urllib.request, json, time, hmac, hashlib, base64, ssl, sys

SECRET = "GovPlatformSecretKey2024ForJwtTokenGenerationAndValidation"
GATEWAY = "http://localhost:8091"
API = f"{GATEWAY}/api/v1"

ctx = ssl.create_default_context()
ctx.check_hostname = False
ctx.verify_mode = ssl.CERT_NONE

def gen_token(uid="1", username="admin"):
    h = base64.urlsafe_b64encode(json.dumps({"alg":"HS256","typ":"JWT"},separators=(',',':')).encode()).rstrip(b'=').decode()
    now = int(time.time())
    p = base64.urlsafe_b64encode(json.dumps({"sub":uid,"username":username,"iss":"gov-platform","iat":now,"exp":now+7200},separators=(',',':')).encode()).rstrip(b'=').decode()
    s = base64.urlsafe_b64encode(hmac.new(SECRET.encode(),f"{h}.{p}".encode(),hashlib.sha256).digest()).rstrip(b'=').decode()
    return f"{h}.{p}.{s}"

TOKEN = gen_token()

def req(method, path, token=None):
    url = f"{API}{path}" if path.startswith("/") else path
    r = urllib.request.Request(url, method=method)
    r.add_header("Content-Type", "application/json")
    if token:
        r.add_header("Authorization", f"Bearer {token}")
    try:
        with urllib.request.urlopen(r, timeout=10, context=ctx) as resp:
            return resp.status, json.loads(resp.read().decode())
    except urllib.error.HTTPError as e:
        try:
            return e.code, json.loads(e.read().decode())
        except:
            return e.code, {"raw": str(e)}
    except Exception as e:
        return 0, {"error": str(e)}

print("=" * 60)
print(f"  全量 API 冒烟测试 ({time.strftime('%Y-%m-%d %H:%M:%S')})")
print("=" * 60)

PASS = FAIL = 0

def t(name, method, path, token=TOKEN, expect=200):
    global PASS, FAIL
    s, b = req(method, path, token)
    ok = s == expect or (isinstance(expect, list) and s in expect)
    mark = "PASS" if ok else "FAIL"
    detail = f"code={b.get('code','?')}" if isinstance(b, dict) else ""
    print(f"  [{mark}] {method:6} {path:<40} -> HTTP {s} (expect {expect}) {detail}")
    if ok: PASS += 1
    else: FAIL += 1

# === 鉴权 ===
print("\n--- 鉴权 ---")
t("无Token拒绝", "GET", "/user/list?pageNum=1&pageSize=1", token=None, expect=401)
t("有Token通过", "GET", "/user/list?pageNum=1&pageSize=1", expect=200)
t("pageSize=0校验", "GET", "/user/list?pageNum=1&pageSize=0", expect=400)

# === gov-user ===
print("\n--- gov-user-service ---")
t("用户分页", "GET", "/user/list?pageNum=1&pageSize=3")
t("用户详情", "GET", "/user/100")
t("部门列表", "GET", "/dept/list")
t("角色列表", "GET", "/role/list")

# === gov-item ===
print("\n--- gov-item-service ---")
t("事项分页", "GET", "/item/list?pageNum=1&pageSize=3")
t("事项详情", "GET", "/item/100")

# === Knife4j ===
print("\n--- 文档 ---")
t("Knife4j", "GET", f"{GATEWAY}/doc.html", expect=[200, 302])

print(f"\n{'='*60}")
print(f"  通过: {PASS}  失败: {FAIL}  总计: {PASS+FAIL}")
print(f"  通过率: {PASS/(PASS+FAIL)*100:.0f}%" if (PASS+FAIL) > 0 else "  无测试")
print(f"{'='*60}")

sys.exit(0 if FAIL == 0 else 1)
