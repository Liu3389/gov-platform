# -*- coding: utf-8 -*-
"""gov-activiti-service 全量接口测试（直连模式 + 模拟网关 X-Roles 头）"""
import json, time, hmac, hashlib, base64, urllib.request, urllib.error, ssl

BASE = "http://localhost:8084"
SECRET = "GovPlatformSecretKey2024ForJwtTokenGenerationAndValidation"

def b64url(data):
    return base64.urlsafe_b64encode(data).rstrip(b'=').decode()

def gen_token(uid, uname, roles=""):
    h = b64url(json.dumps({"alg":"HS256","typ":"JWT"}, separators=(',',':')).encode())
    now = int(time.time())
    p = b64url(json.dumps({"sub":uid,"username":uname,"roles":roles,"iss":"gov-platform","iat":now,"exp":now+7200}, separators=(',',':')).encode())
    sig = b64url(hmac.new(SECRET.encode(),f"{h}.{p}".encode(),hashlib.sha256).digest())
    return f"{h}.{p}.{sig}"

ADMIN = gen_token("1","admin","ROLE_ADMIN")
USER  = gen_token("10002","zhang","")

ctx = ssl.create_default_context(); ctx.check_hostname = False; ctx.verify_mode = ssl.CERT_NONE

def call(method, path, token=None, body=None, roles=""):
    url = BASE + path
    data = json.dumps(body, ensure_ascii=False).encode() if body else None
    req = urllib.request.Request(url, data=data, method=method)
    req.add_header("Content-Type","application/json")
    if token: req.add_header("Authorization",f"Bearer {token}")
    if roles: req.add_header("X-Roles",roles)
    try:
        with urllib.request.urlopen(req,timeout=15,context=ctx) as r:
            return r.status, json.loads(r.read().decode())
    except urllib.error.HTTPError as e:
        try: b = json.loads(e.read().decode())
        except: b = {"raw":str(e)}
        return e.code, b
    except Exception as e:
        return 0, {"error":str(e)}

def test(name, method, path, token, roles, body=None, expected=200, idx=0):
    s, r = call(method, path, token, body, roles)
    ok = "PASS" if (isinstance(expected,list) and s in expected) or (s==expected) else "FAIL"
    tl = "admin" if token==ADMIN else "user" if token==USER else "none"
    print(f"\n{'─'*68}")
    print(f"  [{ok}] #{idx} {name}")
    print(f"  {method} {path}  ->  HTTP {s} (expect {expected})")
    print(f"  Token: {tl}  X-Roles: '{roles}'" if roles else f"  Token: {tl}  X-Roles: (none)")
    if body: print(f"  Body: {json.dumps(body,ensure_ascii=False)[:120]}")
    print(f"  RESP: {json.dumps(r,ensure_ascii=False)}")
    return s == expected or (isinstance(expected,list) and s in expected)

R = []; A = R.append; ROLE = "ROLE_ADMIN"; NR = ""

print("="*68)
print(f"  gov-activiti-service  全量接口测试")
print(f"  {time.strftime('%Y-%m-%d %H:%M:%S')}  |  {BASE}  |  admin=ROLE_ADMIN")
print("="*68)

# 1. TODO（no permission needed）
A(test("待办列表(admin)","GET","/workflow/todo?pageNum=1&pageSize=3&userId=1",ADMIN,NR,idx=1))
A(test("待办列表(user)","GET","/workflow/todo?pageNum=1&pageSize=3&userId=10002",USER,NR,idx=2))
# 2. List (needs workflow:query)
A(test("任务列表(admin)","GET","/workflow/list?pageNum=1&pageSize=3",ADMIN,ROLE,idx=3))
A(test("任务列表(user)","GET","/workflow/list?pageNum=1&pageSize=3",USER,NR,expected=403,idx=4))
# 3. GetById
A(test("ID查询(admin)","GET","/workflow/1",ADMIN,ROLE,expected=[200,404],idx=5))
A(test("ID查询(user)","GET","/workflow/1",USER,NR,expected=403,idx=6))
# 4. Start process (needs workflow:start)
P = {"processKey":"apply_approval_v1","applyNo":"T"+str(int(time.time())),"itemId":10001,"itemName":"测试","userId":1,"userName":"管理员","deptId":20001}
A(test("启动流程(admin)","POST","/workflow/process",ADMIN,ROLE,P,idx=7))
A(test("启动流程(user)","POST","/workflow/process",USER,NR,P,expected=403,idx=8))
# 5. Approve (needs workflow:approve)
A(test("审批(admin)","POST","/workflow/approval",ADMIN,ROLE,{"taskId":"t-001","approvalResult":"1","opinion":"OK"},expected=[200,404],idx=9))
A(test("审批(user)","POST","/workflow/approval",USER,NR,{"taskId":"t-001","approvalResult":"1","opinion":"OK"},expected=403,idx=10))
# 6. Claim (needs workflow:claim)
A(test("认领(admin)","POST","/workflow/task/t-001/claim?userId=1",ADMIN,ROLE,expected=[200,404],idx=11))
A(test("认领(user)","POST","/workflow/task/t-001/claim?userId=2",USER,NR,expected=403,idx=12))
# 7. Delegate (needs workflow:delegate)
A(test("委托(admin)","POST","/workflow/task/t-001/delegation",ADMIN,ROLE,{"toUserId":10003,"delegateReason":"出差"},expected=[200,404],idx=13))
A(test("委托(user)","POST","/workflow/task/t-001/delegation",USER,NR,{"toUserId":10003,"delegateReason":"出差"},expected=403,idx=14))
# 8. Remind (needs workflow:remind)
A(test("催办(admin)","POST","/workflow/reminder?reminderBy=1",ADMIN,ROLE,{"instanceId":"i-001","reminderContent":"尽快"},expected=[200,404],idx=15))
A(test("催办(user)","POST","/workflow/reminder?reminderBy=2",USER,NR,{"instanceId":"i-001","reminderContent":"尽快"},expected=403,idx=16))
# 9. Delete (needs workflow:delete)
A(test("删除(admin)","DELETE","/workflow/999",ADMIN,ROLE,expected=[200,404],idx=17))
A(test("删除(user)","DELETE","/workflow/999",USER,NR,expected=403,idx=18))
# 10. No token
A(test("无Token列表","GET","/workflow/list?pageNum=1&pageSize=1",None,NR,expected=[401,403],idx=19))
A(test("无Token启动","POST","/workflow/process",None,NR,P,expected=[401,403],idx=20))

p = sum(R); t = len(R)
print(f"\n{'='*68}")
print(f"  PASS {p}/{t} ({p/t*100:.0f}%)  |  {'' if p==t else 'FAIL ' + str(t-p) + ' items'}")
print(f"{'='*68}")
