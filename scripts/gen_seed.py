#!/usr/bin/env python3
"""全量测试种子数据生成器 v4 — 基于实际表结构自动适配"""
import subprocess, json, random, os, re
from datetime import datetime, timedelta

random.seed(42)
NOW = datetime.now()
D = lambda d=0: (NOW - timedelta(days=d)).strftime('%Y-%m-%d %H:%M:%S')
DT = lambda d=0: (NOW - timedelta(days=d)).strftime('%Y-%m-%d')
EX = lambda d=0: (NOW + timedelta(days=d)).strftime('%Y-%m-%d 00:00:00')
DB = {"host":"127.0.0.1","port":3307,"user":"root","password":"root123456"}

def q(sql):
    r = subprocess.run(["mysql","-h",DB["host"],"-P",str(DB["port"]),"-u",DB["user"],f"-p{DB['password']}","-N","-e",sql],
                       capture_output=True, text=True)
    return r.stdout.strip()

def Q(v):
    if v is None: return "NULL"
    if isinstance(v, (int, float)): return str(v)
    if isinstance(v, bool): return "1" if v else "0"
    return "'" + str(v).replace("'", "\\'") + "'"

# 获取所有表结构
tables = {}
for line in q("SELECT table_schema,table_name,GROUP_CONCAT(column_name ORDER BY ordinal_position) FROM information_schema.columns WHERE table_schema LIKE 'gov_%%' AND table_name NOT LIKE 'ACT_%%' GROUP BY table_schema,table_name ORDER BY table_schema,table_name").split("\n"):
    parts = line.split("\t", 2)
    if len(parts) < 3: continue
    db, tbl, cols = parts
    tables.setdefault(db, {})[tbl] = cols.split(",")

out = []
out.append("-- ============================================")
out.append(f"-- 全量测试种子数据 v4 — 基于实际表结构")
out.append(f"-- 生成: {NOW.strftime('%Y-%m-%d %H:%M:%S')}")
out.append(f"-- 覆盖 {sum(len(v) for v in tables.values())} 张表")
out.append("SET FOREIGN_KEY_CHECKS = 0;")
out.append("")

def table(db, tbl, rows, cols=None):
    # Auto-detect swapped args: if cols is a list of tuples
    if cols is not None and len(cols) > 0 and isinstance(cols[0], tuple):
        rows, cols = cols, rows
    if not rows: return
    # Always use actual table columns as authoritative
    actual_cols = tables.get(db, {}).get(tbl, [])
    if not actual_cols:
        if not cols: return
        actual_cols = cols
    if not actual_cols: return
    
    # If cols provided, map row values from provided cols to actual cols
    out.append(f"USE `{db}`;")
    out.append(f"DELETE FROM `{tbl}` WHERE id >= 100;")
    c = ",".join(f"`{x}`" for x in actual_cols)
    
    # Build a default value map for common columns
    defaults = {"create_by":1, "update_by":1, "create_time":D(30), "update_time":D(30), "deleted":0, "status":"1"}
    
    for row in rows:
        if cols and len(cols) <= len(row):
            # Map provided cols to actual cols
            col_map = dict(zip(cols, row))
        elif not cols and len(row) == len(actual_cols):
            col_map = dict(zip(actual_cols, row))
        else:
            if len(row) != len(actual_cols) and not cols:
                print(f"  WARN: {db}.{tbl} row has {len(row)} values but table has {len(actual_cols)} cols, skipping")
                continue
            col_map = dict(zip(actual_cols if not cols else cols, row))
        
        vals = []
        for ac in actual_cols:
            if ac in col_map:
                vals.append(Q(col_map[ac]))
            elif ac in defaults:
                vals.append(Q(defaults[ac]))
            else:
                vals.append("NULL")
        out.append(f"INSERT INTO `{tbl}`({c}) VALUES({','.join(vals)});")
    out.append("")

dept_names = ["行政审批局","公安局","税务局","市场监管局","自然资源局","卫健局","住建局","人社局","教育局","交通局"]
dept_ids = [100,101,102,103,104,105,106,107,108,109]
lastnames = ["张","李","王","赵","孙","周","吴","郑","钱","陈","刘","黄","杨","朱","马","胡","郭","何","高"]
item_names = [
    "建设用地规划许可证核发","食品经营许可","公共场所卫生许可","居民身份证办理","不动产登记",
    "税务登记","房屋预售许可","学历认证","企业名称预先核准","建设工程规划验收",
    "消防安全检查","环评审批","排污许可","取水许可","河道采砂许可",
    "林木采伐许可","动物检疫","种畜禽经营许可","农药经营许可","兽药经营许可",
    "道路货运许可","客运班线许可","驾校备案","网约车许可","危化品运输许可",
    "医师执业注册","护士执业注册","医疗机构许可","母婴保健许可","放射诊疗许可",
    "教师证认定","办学许可","出版物经营许可","网吧许可","演出经纪许可",
    "建筑施工许可","商品房预售许可","物业资质备案","燃气经营许可","垃圾清运许可",
    "社保登记","医保定点","低保申请","残疾人证","老年人优待证",
    "人才引进","档案转递","劳动争议","外国人工作许可","港澳通行证"
]

# ======================== gov_user ========================
table("gov_user", "t_dept_info", [(100+i, n, f"DEPT{100+i}", 0, 1, None, None, None, i+1, 1, D(30), D(30), 1, 1, 0) for i,n in enumerate(dept_names)])

roles = [("系统管理员","ROLE_ADMIN"),("窗口人员","ROLE_WINDOW"),("审批人员","ROLE_APPROVER"),
         ("部门领导","ROLE_LEADER"),("普通用户","ROLE_USER"),("督查人员","ROLE_SUPERVISOR"),
         ("数据管理员","ROLE_DATA_ADMIN"),("系统审计员","ROLE_AUDITOR")]
table("gov_user", "t_role_info",
    [(100+i, n, c, 1, None, D(30), D(30), 1, 1, 0) for i,(n,c) in enumerate(roles)],
    ["id","role_name","role_code","status","remark","create_time","update_time","create_by","update_by","deleted"])

user_rows = []
for i in range(50):
    uid = 100+i
    ln = lastnames[i%len(lastnames)]
    un = f"user{uid:03d}" if i>=12 else f"{ln.lower()}{uid}"
    rn = f"{ln}{['明','华','强','伟','芳','丽','军','勇','敏','静'][i%10]}"
    st = [0,0,0,0,0,0,0,0,0,1,1,2][i%12]
    dp = random.choice(dept_ids)
    gd = i%3
    user_rows.append((uid, un, "$2a$10$placeholder", f"1380000{uid:04d}", rn,
        f"1101011990{i%12+1:02d}{i%28+1:02d}{uid:04d}", f"{un}@test.com",
        None, gd, st, D(random.randint(1,30)), f"192.168.{i%256}.{i//256+1}",
        D(30), D(30), 1, 1, 0))
table("gov_user", "t_user_info",
    ["id","username","password","phone","real_name","id_card","email","avatar","gender","status",
     "last_login_time","last_login_ip","create_time","update_time","create_by","update_by","deleted"], user_rows)

ur_rows = []
for i in range(50):
    uid = 100+i
    for j in range(1+(i%3)):
        ur_rows.append((1000+i*3+j, uid, 100+(i+j)%8, D(30), D(30), 1, 1, 0))
table("gov_user", "t_user_role", ["id","user_id","role_id","create_time","update_time","create_by","update_by","deleted"], ur_rows)

table("gov_user", "t_role_menu",
    [(100+i, 100+i%8, 1+i%13, D(30), D(30), 1, 1, 0) for i in range(80)],
    ["id","role_id","menu_id","create_time","update_time","create_by","update_by","deleted"])

table("gov_user", "t_config",
    [(100+i, k, v, t, r, D(365), D(365), 1, 1, 0) for i,(k,v,t,r) in enumerate([
        ("SYS_NAME","智慧政务平台","system","系统名称"),
        ("SYS_LOGO","/logo.png","system","系统LOGO"),
        ("SMS_PROVIDER","aliyun","sms","短信通道"),
        ("EMAIL_HOST","smtp.gov.cn","email","邮件服务器"),
        ("AUDIT_TIMEOUT","72","workflow","审批超时"),
        ("QUEUE_MAX","50","reception","排队上限"),
        ("LICENSE_SEAL_KEY","seal_key_001","license","签章密钥"),
        ("FILE_MAX_SIZE","10485760","system","文件上限"),
        ("LOGIN_MAX_RETRY","5","security","重试上限"),
        ("TOKEN_EXPIRE","7200","security","过期秒数"),
    ])],
    ["id","config_key","config_value","config_type","remark","create_time","update_time","create_by","update_by","deleted"])

table("gov_user", "t_dict_data",
    [(100+i, dt, f"{dt}_{vi}", f"{dt}-值{vi}", str(vi), "0", vi, f"{dt}字典", 1, D(30), D(30), 1, 1, 0)
     for i,(dt,vi) in enumerate([(dt,vi) for dt in ["ITEM_TYPE","GENDER","COMPLAINT_TYPE","NOTICE_TYPE","POLICY_TYPE","LICENSE_TYPE","QUEUE_STATUS","RECORD_STATUS","WORK_STATUS","MSG_CHANNEL"] for vi in range(1,4)])],
    ["id","dict_type","dict_code","dict_name","dict_value","parent_code","sort","remark","status","create_time","update_time","create_by","update_by","deleted"])

table("gov_user", "t_login_log",
    [(100+i, random.randint(100,149), f"user{random.randint(100,149):03d}", D(random.randint(0,30)),
      f"192.168.{random.randint(1,255)}.{random.randint(1,255)}",
      random.choice(["本地","北京","上海","广州"]),
      random.choice([1,2,3]), random.choice([1,1,1,1,0]),
      None if random.random()>0.1 else "密码错误",
      D(random.randint(1,30)), D(random.randint(1,30)), 1, 1, 0)
     for i in range(100)],
    ["id","user_id","username","login_time","login_ip","login_location","login_type","login_status","login_msg","create_time","update_time","create_by","update_by","deleted"])

table("gov_user", "t_file_info",
    [(100+i, f"文件{i+1}.pdf", f"/files/2024/{i+1}.pdf", random.choice(["pdf","jpg","png"]),
      1024*random.randint(10,10000), random.randint(100,149), D(random.randint(1,90)),
      random.choice(["record","license","complaint","apply"]), random.randint(100,200), D(random.randint(1,90)), 0)
     for i in range(20)],
    ["id","file_name","file_path","file_type","file_size","upload_user","upload_time","business_type","business_id","create_time","deleted"])

table("gov_user", "t_ca_certificate",
    [(100+i, 100+i*3, f"CA-{20240000+i+1:06d}", f"证书内容{i}", random.randint(1,3), random.choice(["CA中心","数安中心","第三方CA"]),
      EX(random.randint(-30,365*3)), 1, D(random.randint(30,365)), D(random.randint(1,30)), 1, 1, 0)
     for i in range(15)],
    ["id","user_id","cert_sn","cert_content","cert_type","issuer","expire_time","status","create_time","update_time","create_by","update_by","deleted"])

table("gov_user", "t_user_realname",
    [(100+i, 100+i, f"用户{100+i}", f"11010119900101{100+i:04d}", random.randint(1,3),
      f"/files/front_{100+i}.jpg", f"/files/back_{100+i}.jpg" if random.random()>0.5 else None,
      random.choice([1,1,1,1,0,2,3]),
      D(random.randint(1,30)) if random.random()>0.5 else None,
      101 if random.random()>0.5 else None,
      None if random.random()>0.7 else "信息不一致",
      D(random.randint(1,60)), D(random.randint(1,30)), 1, 1, 0)
     for i in range(30)],
    ["id","user_id","real_name","id_card","cert_type","cert_front_url","cert_back_url","verify_status","verify_time","verify_by","verify_remark","create_time","update_time","create_by","update_by","deleted"])

# ======================== gov_item ========================
table("gov_item", "t_item_base",
    [(100+i, f"ITEM-{2024000+i+1:04d}", item_names[i], random.choice(dept_ids), 1,
      [1,1,1,1,1,2,2,3][i%8], [1,1,2,3][i%4], [1,1,1,2,3][i%5],
      random.choice([1,3,5,7,10,15,20,30]),
      random.choice([0,0,0,1]), {0:"免费",1:"按标准收费",2:"工本费20元",3:"按面积收费",4:"按项目收费"}[i%5] if random.choice([0,0,0,1]) else "免费",
      random.choice([1,1,0]), random.choice([1,1,1,1,0]), [1,1,1,1,1,0,2][i%7], 1, i+1, D(30), D(30), 1, 1, 0)
     for i in range(50)],
    ["id","item_code","item_name","dept_id","category_id","item_type","item_level","implement_type","time_limit","fee_flag","fee_standard","online_flag","window_flag","status","version","sort","create_time","update_time","create_by","update_by","deleted"])

table("gov_item", "t_item_category",
    [(100+i, n, c, p, l, s, 1, D(30), D(30), 1, 1, 0) for i,(n,c,p,l,s) in enumerate([
        ("行政许可","XZXK",0,1,1),("行政确认","XZQR",0,1,2),("公共服务","GGFW",0,1,3),
        ("行政备案","XZBA",0,1,4),("建设规划","JSGH",100,2,1),("食品药品","SPYP",100,2,2),
        ("社会事务","SHSW",102,2,1),("交通管理","JTGL",100,2,3),("自然资源","ZRZY",101,2,1),
        ("教育科技","JYKJ",102,2,2),("公安消防","GAXF",100,2,4),("人力资源","RLZY",102,2,3),
    ])],
    ["id","category_name","category_code","parent_id","category_level","sort","status","create_time","update_time","create_by","update_by","deleted"])

table("gov_item", "t_item_dept",
    [(100+i, 100+random.randint(0,49), random.choice(dept_ids), random.randint(1,3), random.randint(1,5), D(30), 0) for i in range(80)],
    ["id","item_id","dept_id","dept_type","sort","create_time","deleted"])

table("gov_item", "t_item_form",
    [(100+i, 100+random.randint(0,49), f"表单{i+1}", f'{{"fields":["f1","f2","f3"]}}', random.randint(1,3), 1 if random.random()>0.2 else 0, D(30), D(30), 0) for i in range(100)],
    ["id","item_id","form_name","form_schema","form_type","is_required","create_time","update_time","deleted"])

guide_rows = []
for i in range(50):
    guid = 100+i
    tl = random.choice([1,3,5,7,10,15,20,30])
    ff = "免费" if random.random()>0.3 else f"收费{random.randint(10,500)}元"
    guide_rows.append((guid, 100+i,
        f"办事指南内容{i}", "符合法定条件,材料齐全,在有效期范围内", "材料不全,不符合条件,存在违规记录",
        tl, ff, random.choice(["批文","证照","证明文件","电子证照"]),
        f"办理流程描述{i}", f"010-8880{i:04d}", f"某某路{i+1}号政务大厅{i+1}号窗口",
        "法律法规依据", None, D(30), D(30), 0))
table("gov_item", "t_item_guide",
    ["id","item_id","guide_content","accept_condition","reject_condition","time_limit","fee_info","result_type","process_desc","consult_phone","consult_address","legal_basis","online_url","create_time","update_time","deleted"], guide_rows)

table("gov_item", "t_item_material",
    [(100+i, 100+random.randint(0,49), f"材料需求{i+1}", f"MAT-{2024000+i+1:05d}", None,
      random.randint(1,3), 1 if random.random()>0.3 else 0, random.randint(1,3),
      f"/files/sample_{i}.pdf" if random.random()>0.5 else None,
      f"/files/tpl_{i}.docx" if random.random()>0.5 else None, None, None,
      f"备注{i}" if random.random()>0.7 else None, random.randint(1,10), D(30), D(30), 0)
     for i in range(120)],
    ["id","item_id","material_name","material_code","material_type_name","material_type","is_required","source_type","sample_url","template_url","paper_num","special_require","remark","sort","create_time","update_time","deleted"])

table("gov_item", "t_item_process",
    [(100+i, 100+random.randint(0,49), f"PROC-{i+1:03d}", random.choice(["标准审批","加速审批","简易流程","多部门会签","领导审核"]),
      1 if random.random()>0.2 else 0, random.choice([1,1,0]), D(30), D(30), 0) for i in range(60)],
    ["id","item_id","process_key","process_name","is_default","status","create_time","update_time","deleted"])

table("gov_item", "t_item_version",
    [(100+i, 100+random.randint(0,49), i%5+1, f"版本{i%5+1}.0",
      D(random.randint(1,365)), random.randint(100,149),
      random.choice([1,1,1,0,2]), f"变更内容{i%5+1}" if i%5+1>1 else None,
      D(random.randint(1,365)), D(random.randint(1,30)), 0) for i in range(40)],
    ["id","item_id","version_no","version_name","publish_time","publish_by","status","change_log","create_time","update_time","deleted"])

# ======================== gov_reception ========================
table("gov_reception", "t_reception_window",
    [(100+i, f"W{i+1:03d}", f"{random.choice(['综合','公安','税务','市监','不动产','卫健'])}窗口{i+1}",
      random.choice(dept_ids), None, None, random.choice(["综合","专业","绿色通道"]),
      ["1","1","1","1","1","0","2"][i%7], D(30), D(30), 0) for i in range(15)],
    ["id","window_no","window_name","dept_id","staff_id","staff_name","window_type","status","create_time","update_time","deleted"])

record_rows = []
for i in range(100):
    rid = 100+i
    st = ["0","1","2","3","4","1","2","3","0","0","2","2","3","3","1","2","3","4","1","3"][i%20]
    item_id = random.randint(100,149)
    user_id = random.randint(100,149)
    dp = random.choice(dept_ids)
    wid = random.choice([None,100,101,102,103,104]) if st in ["0","1"] else random.randint(100,114)
    accept = D(random.randint(1,60))
    finish = D(random.randint(1,30)) if st=="3" else None
    pi = f"PROC-{rid+1000:04d}" if st in ["2","3","4"] else None
    rr = random.choice([None,None,None,"材料不全","不符合条件","信息有误"]) if st=="4" else None
    record_rows.append((rid, f"BJ-{2024000+rid+1:05d}", item_id, user_id, dp, wid,
        None if wid is None else 101, None, st, accept, finish, pi, None, rr, None, None,
        D(random.randint(1,60)), D(random.randint(1,60)), 0))
table("gov_reception", "t_reception_record",
    ["id","apply_no","item_id","user_id","dept_id","window_id","operator_id","counter_id","status","accept_time","finish_time","process_instance_id","timeout_time","reject_reason","photo_url","remark","create_time","update_time","deleted"], record_rows)

table("gov_reception", "t_reception_queue",
    [(200+i, f"Q-{20240000+i+1:06d}", random.randint(100,149), f"用户{random.randint(100,149)}",
      random.randint(100,114), f"W{random.randint(1,15):03d}", random.randint(100,149), f"事项{random.randint(100,149)}",
      D(random.randint(0,1)), ["0","0","0","1","1","2"][i%6], 0,
      D(random.randint(0,30)), D(random.randint(0,30)), 0) for i in range(30)],
    ["id","queue_no","user_id","user_name","window_id","window_no","item_id","item_name","queue_time","status","priority","create_time","update_time","deleted"])

mat_rows, mid = [], 300
for rid in range(100,200):
    if random.random()<0.3: continue
    for mi in range(random.randint(1,3)):
        vs = random.choice(["0","0","1","1"])
        mat_rows.append((mid, rid, 100+random.randint(0,119), f"材料{mid}", "document", None, None, vs,
            D(random.randint(1,5)) if vs=="1" else None, 101 if vs=="1" else None, "通过" if vs=="1" else None,
            D(random.randint(1,60)), D(random.randint(1,60)), 1, 1, 0))
        mid += 1
table("gov_reception", "t_reception_material",
    ["id","record_id","material_id","material_name","material_type","file_url","file_name","verify_status","verify_time","verify_by","verify_remark","create_time","update_time","create_by","update_by","deleted"], mat_rows)

log_rows2, lid = [], 400
for rid in range(100,200):
    for _ in range(random.randint(1,3)):
        log_rows2.append((lid, rid, random.choice(["受理","转办","审批通过","审批退回","补充材料","办结","驳回"]),
            D(random.randint(1,50)), random.choice([101,102,103,104,105]), ["张三","李四","王五","赵六","孙七"][lid%5],
            f"备注{lid}", D(random.randint(1,50)), D(random.randint(1,50)), 0))
        lid += 1
table("gov_reception", "t_reception_log",
    ["id","record_id","action_type","action_time","operator_id","operator_name","remark","create_time","update_time","deleted"], log_rows2)

table("gov_reception", "t_reception_form_data",
    [(100+i, 100+random.randint(0,99), 100+random.randint(0,99), f'{{"data":"{i}"}}', D(random.randint(1,60)), D(random.randint(1,60)), random.choice([101,102,103,104,105]), random.choice([101,102,103,104,105]), 0) for i in range(80)],
    ["id","record_id","form_id","form_data","create_time","update_time","create_by","update_by","deleted"])

table("gov_reception", "t_reception_output",
    [(100+i, 100+random.randint(0,99), random.choice(["电子证照","纸质证照","证明文件"]), f"产出物{i+1}", 100+random.randint(0,29), f"LIC-{2024000+random.randint(100,129)+1:05d}", D(random.randint(1,60)), random.randint(100,149),
      random.choice(["自取","邮寄","下载"]), D(random.randint(1,30)) if random.random()>0.3 else None,
      random.randint(100,149) if random.random()>0.3 else None, f"领取备注{i}" if random.random()>0.5 else None,
      random.choice(["1","1","1","0","2"]), D(random.randint(1,60)), D(random.randint(1,60)), 1, 1, 0) for i in range(30)],
    ["id","record_id","output_type","output_name","license_id","license_no","output_time","output_by","pickup_type","pickup_time","pickup_by","pickup_remark","status","create_time","update_time","create_by","update_by","deleted"])

# ======================== gov_activiti ========================
inst_ids = [f"PROC-{1000+i:04d}" for i in range(20)]
task_ids = [f"TASK-{2000+i:04d}" for i in range(20)]
table("gov_activiti", "t_workflow_countersign",
    [(100+i, random.choice(task_ids), random.choice(inst_ids), random.randint(100,149), f"用户{random.randint(100,149)}", random.choice(dept_ids), f"部门{random.choice(dept_ids)}",
      random.choice(["1","1","1","2","3",""]), D(random.randint(1,30)) if random.random()>0.3 else None, f"会签意见{i}" if random.random()>0.5 else None,
      D(random.randint(1,30)), D(random.randint(1,30)), 1, 1, 0) for i in range(20)],
    ["id","task_id","instance_id","user_id","user_name","dept_id","dept_name","sign_result","sign_time","sign_opinion","create_time","update_time","create_by","update_by","deleted"])

table("gov_activiti", "t_workflow_delegate",
    [(100+i, random.choice(task_ids), random.choice(inst_ids), random.randint(100,149), f"用户{random.randint(100,149)}", random.randint(100,149), f"用户{random.randint(100,149)}",
      random.choice(["转办","代理","委托"]), D(random.randint(1,30)), f"委托原因{i}", random.choice(["1","1","0","2"]),
      D(random.randint(1,30)), D(random.randint(1,30)), 1, 1, 0) for i in range(15)],
    ["id","task_id","instance_id","from_user_id","from_user_name","to_user_id","to_user_name","delegate_type","delegate_time","delegate_reason","status","create_time","update_time","create_by","update_by","deleted"])

table("gov_activiti", "t_workflow_history",
    [(100+i, f"INST-{3000+i:04d}", f"APPLY_APPROVAL_V{i%3+1}", f"BJ-{2024000+1+i:05d}",
      D(random.randint(1,180)), random.randint(100,149), random.randint(3600,86400*7),
      random.randint(3,12), random.randint(1,5), random.randint(0,2),
      random.choice(["通过","驳回","部分通过"]), f"归档备注{i}",
      D(random.randint(1,180)), D(random.randint(1,30)), 1, 1, 0) for i in range(20)],
    ["id","instance_id","process_key","apply_no","archive_time","archive_by","total_duration","node_count","approve_count","reject_count","result","remark","create_time","update_time","create_by","update_by","deleted"])

table("gov_activiti", "t_workflow_opinion",
    [(100+i, random.choice(task_ids), random.choice(inst_ids), f"BJ-{2024000+i+1:05d}", random.randint(100,149), f"用户{random.randint(100,149)}",
      random.choice(["同意","驳回","转办","退回修改","加签"]), f"审批意见{i}", D(random.randint(1,60)),
      f"用户{random.randint(100,149)}", D(random.randint(1,60)), D(random.randint(1,30)), 1, 1, 0) for i in range(30)],
    ["id","task_id","instance_id","apply_no","operator_id","operator_name","opinion_type","opinion_content","operate_time","next_assignee","create_time","update_time","create_by","update_by","deleted"])

table("gov_activiti", "t_workflow_reminder",
    [(100+i, random.choice(inst_ids), random.choice(task_ids), f"BJ-{2024000+i+1:05d}", random.choice(["超时提醒","临期提醒","预警通知","督办通知"]),
      random.choice(["普通","重要","紧急"]), D(random.randint(1,60)), random.randint(100,149), f"提醒内容{i}",
      random.choice(["1","1","0","2"]), D(random.randint(1,60)), D(random.randint(1,30)), 1, 1, 0) for i in range(25)],
    ["id","instance_id","task_id","apply_no","reminder_type","reminder_level","reminder_time","reminder_by","reminder_content","status","create_time","update_time","create_by","update_by","deleted"])

# ======================== gov_license ========================
cat_rows_l = [
    (100,"CAT-001","建设用地规划许可证","行政许可",100,"行政审批局",2,None,None,None,None,1,1,1,D(30),D(30),0),
    (101,"CAT-002","食品经营许可证","行政许可",100,"行政审批局",5,None,None,None,None,1,1,1,D(30),D(30),0),
    (102,"CAT-003","卫生许可证","行政许可",105,"卫健局",4,None,None,None,None,1,1,1,D(30),D(30),0),
    (103,"CAT-004","不动产登记证","行政确认",104,"自然资源局",0,None,None,None,None,1,1,1,D(30),D(30),0),
    (104,"CAT-005","居民身份证","公共服务",101,"公安局",10,None,None,None,None,0,1,1,D(30),D(30),0),
    (105,"CAT-006","驾驶证","公共服务",101,"公安局",6,None,None,None,None,0,1,1,D(30),D(30),0),
    (106,"CAT-007","营业执照","行政许可",103,"市场监管局",0,None,None,None,None,1,1,1,D(30),D(30),0),
    (107,"CAT-008","办学许可证","行政许可",108,"教育局",4,None,None,None,None,1,1,1,D(30),D(30),0),
]
table("gov_license", "t_license_catalog",
    ["id","catalog_code","catalog_name","catalog_type","dept_id","dept_name","valid_years","template_url","sign_config","cert_format","print_params","sign_flag","qr_flag","status","create_time","update_time","deleted"], cat_rows_l)

table("gov_license", "t_license_data",
    [(100+i, f"LIC-{2024000+i+1:05d}", random.choice([100,101,102,103,104,105,106,107]), f"证照名称{random.randint(100,107)}",
      random.randint(100,149), f"用户{random.randint(100,149)}", f"11010119900101{random.randint(100149,149149):05d}",
      f"BJ-{100+i}", f'{{"info":"证照{i}"}}',
      f"/files/lic{i}.pdf" if random.random()>0.2 else None,
      f"QR-{2024000+i+1:05d}" if random.random()>0.3 else None,
      f"https://verify.gov.cn/LIC-{2024000+i+1:05d}" if random.random()>0.3 else None,
      random.choice(["1","1","1","2","3"]), None,
      EX(random.randint(365,365*5)),
      D(random.randint(1,60)) if random.random()>0.3 else None, random.randint(100,149) if random.random()>0.5 else None,
      D(random.randint(1,60)) if random.random()>0.7 else None, "过期作废" if random.random()>0.7 else None,
      D(random.randint(1,60)), D(random.randint(1,30)), 0) for i in range(30)],
    ["id","license_no","catalog_id","catalog_name","user_id","user_name","user_id_card","apply_no","license_content","file_url","qr_code","qr_content","sign_status","sign_num","expire_time","sign_time","sign_by","cancel_time","cancel_reason","create_time","update_time","deleted"])

table("gov_license", "t_license_auth",
    [(100+i, 100+random.randint(0,29), random.randint(100,149), random.randint(100,149), f"目标{random.randint(100,149)}",
      random.choice(["部门","企业","个人","机构"]), random.choice(["全部","脱敏","摘要"]),
      D(random.randint(1,60)), EX(random.randint(30,365)) if random.random()>0.3 else None,
      D(random.randint(1,30)) if random.random()>0.7 else None, random.choice(["1","1","1","0","2"]),
      D(random.randint(1,60)), D(random.randint(1,30)), 1, 1, 0) for i in range(20)],
    ["id","license_id","auth_user_id","auth_target_id","auth_target_name","auth_type","auth_scope","auth_time","expire_time","cancel_time","status","create_time","update_time","create_by","update_by","deleted"])

table("gov_license", "t_license_sign",
    [(100+i, 100+random.randint(0,29), random.choice(["电子签章","数字签名","手写签名"]),
      random.randint(100,149), f"用户{random.randint(100,149)}", D(random.randint(1,60)),
      random.choice(["1","1","1","2",""]), f"CA-{20240000+i+1:06d}" if random.random()>0.3 else None,
      f'{{"sign":"data{i}"}}' if random.random()>0.5 else None,
      D(random.randint(1,60)), D(random.randint(1,30)), 1, 1, 0) for i in range(25)],
    ["id","license_id","sign_type","sign_user","sign_user_name","sign_time","sign_result","sign_cert_sn","sign_data","create_time","update_time","create_by","update_by","deleted"])

table("gov_license", "t_license_template",
    [(200+cat[0]*2+vn, cat[0], f"模板-{cat[3]}-V{vn}", f"/templates/{cat[0]}_v{vn}.pdf",
      random.choice(["OFD","PDF","HTML"]),
      f'{{"fields":["name","date","content"]}}',
      f'{{"sign":"pos"}}', vn, "1", D(30), D(30), 1, 1, 0)
     for cat in cat_rows_l for vn in range(1,3)],
    ["id","catalog_id","template_name","template_url","template_type","field_config","sign_config","version","status","create_time","update_time","create_by","update_by","deleted"])

table("gov_license", "t_license_verify",
    [(100+i, 100+random.randint(0,29), f"LIC-{2024000+i+1:05d}", random.randint(100,149), f"用户{random.randint(100,149)}",
      D(random.randint(1,60)), random.choice(["1","1","1","0","2"]),
      random.choice(["政务窗口","企业查验","个人查询","执法检查","银行开户"]),
      f"192.168.{random.randint(1,255)}.{random.randint(1,255)}",
      f"核验备注{i}" if random.random()>0.5 else None,
      D(random.randint(1,60)), D(random.randint(1,30)), 1, 1, 0) for i in range(30)],
    ["id","license_id","license_no","verify_user","verify_user_name","verify_time","verify_result","verify_scene","verify_ip","verify_remark","create_time","update_time","create_by","update_by","deleted"])

# ======================== gov_complaint ========================
table("gov_complaint", "t_complaint_work",
    [(100+i, f"TS-{2024000+i+1:05d}", random.randint(100,149), f"用户{random.randint(100,149)}",
      f"1380000{random.randint(100,149):04d}", f"11010119900101{random.randint(100,149):04d}",
      random.choice([100,101,102,103]), random.choice(["政务服务","城市管理","环境保护","教育医疗"]),
      ["投诉","建议","举报"][i%3], f"投诉主题{i}", f"投诉内容{i}", None,
      random.choice(dept_ids), f"部门{random.choice(dept_ids)}",
      random.randint(100,149), f"处理人{random.randint(100,149)}",
      ["0","0","1","1","2","2","3"][i%7],
      D(random.randint(1,60)), D(random.randint(1,50)) if ["0","0","1","1","2","2","3"][i%7]!="0" else None,
      D(random.randint(1,40)) if ["0","0","1","1","2","2","3"][i%7] in ("2","3") else None,
      D(random.randint(1,30)) if ["0","0","1","1","2","2","3"][i%7]=="3" else None,
      random.choice([1,2,3,4,5,None]) if ["0","0","1","1","2","2","3"][i%7]=="3" else None,
      random.choice([None,"专员","自动","推送","调度"]),
      D(random.randint(1,30)) if random.random()>0.5 else None,
      f"备注{i}" if random.random()>0.5 else None,
      D(random.randint(1,60)), D(random.randint(1,60)), 0) for i in range(50)],
    ["id","work_no","user_id","user_name","user_phone","user_id_card","category_id","category_name","complaint_type","title","content","images","dept_id","dept_name","handler_id","handler_name","status","submit_time","assign_time","handle_time","finish_time","satisfaction","assign_type","satisfaction_time","remark","create_time","update_time","deleted"])

table("gov_complaint", "t_complaint_handle",
    [(100+i, 100+random.randint(0,49), random.randint(100,149), f"处理人{random.randint(100,149)}",
      random.choice(["受理","转办","退回","答复","办结","督办"]), f"处理内容{i}",
      D(random.randint(1,60)), random.choice(dept_ids), f"部门{random.choice(dept_ids)}",
      D(random.randint(1,60)), D(random.randint(1,30)), 1, 1, 0) for i in range(40)],
    ["id","work_id","handler_id","handler_name","handle_type","handle_content","handle_time","next_dept_id","next_dept_name","create_time","update_time","create_by","update_by","deleted"])

hot_kws = ["办事效率","服务态度","材料繁多","系统故障","流程繁琐","审批太慢","窗口太少","排队太久","信息不透明","投诉无门","收费高","来回跑","材料被拒","看脸色办事","数据不一致","跨部门协调","踢皮球","标准不一","证件过期","系统卡顿"]
table("gov_complaint", "t_complaint_hotspot",
    [(100+i, hot_kws[i%20], random.randint(10,500), DT(random.randint(1,30)), random.choice([100,101,102,103]), random.choice(["上升","下降","平稳","波动"]), D(random.randint(1,30)), D(random.randint(1,30)), 1, 1, 0) for i in range(20)],
    ["id","keyword","keyword_count","stat_date","category_id","trend","create_time","update_time","create_by","update_by","deleted"])

table("gov_complaint", "t_complaint_supervise",
    [(100+i, 100+random.randint(0,49), random.choice(["一般","重要","紧急","特急"]),
      D(random.randint(1,60)), random.randint(100,149), f"督办内容{i}",
      D(random.randint(1,15)), random.choice(["1","1","0","2"]),
      D(random.randint(1,60)), D(random.randint(1,30)), 1, 1, 0) for i in range(15)],
    ["id","work_id","supervise_level","supervise_time","supervise_by","supervise_content","deadline","status","create_time","update_time","create_by","update_by","deleted"])

table("gov_complaint", "t_suggestion",
    [(100+i, f"SG-{2024000+i+1:05d}", random.randint(100,149), f"用户{random.randint(100,149)}",
      f"建议标题{i}", f"建议内容{i}", random.choice(["政府服务","城市管理","环境保护","教育医疗","交通出行"]),
      random.choice(["1","1","0","2","3"]),
      f"回复内容{i}" if random.random()>0.5 else None,
      D(random.randint(1,30)) if random.random()>0.5 else None,
      random.randint(100,149) if random.random()>0.5 else None,
      D(random.randint(1,60)), D(random.randint(1,30)), 1, 1, 0) for i in range(25)],
    ["id","suggestion_no","user_id","user_name","title","content","suggestion_type","status","reply_content","reply_time","reply_by","create_time","update_time","create_by","update_by","deleted"])

# ======================== gov_open ========================
table("gov_open", "t_open_notice",
    [(100+i, f"N-{2024000+i+1:05d}", f"通知标题{i}", f"通知内容{i}...",
      ["通知","公告","公示"][i%3], random.choice(dept_ids), f"部门{random.choice(dept_ids)}",
      random.randint(100,149), f"发布人{i%50}",
      D(random.randint(1,90)), D(random.randint(-60,180)) if random.random()>0.3 else None,
      random.choice([0,0,0,0,1]), random.randint(10,5000),
      ["1","1","1","1","0","2"][i%6], D(random.randint(1,90)), D(random.randint(1,90)), 0) for i in range(30)],
    ["id","notice_code","title","content","notice_type","publish_dept_id","publish_dept_name","publish_user_id","publish_user_name",
     "publish_time","expire_time","top_flag","view_count","status","create_time","update_time","deleted"])

table("gov_open", "t_open_policy",
    [(100+i, f"P-{2024000+i+1:05d}", f"政策{i}", random.choice(["行政法规","部门规章","行政规范"]),
      random.choice(dept_ids), f"部门{random.choice(dept_ids)}",
      D(random.randint(1,180)), D(random.randint(1,170)) if i%5!=2 else None,
      ["1","1","1","2","3"][i%5], f"政策内容{i}",
      f"/files/p{i}.pdf" if random.random()>0.3 else None, f"关键词{i}",
      random.randint(10,2000), ["1","1","1","0","2"][i%5],
      D(random.randint(1,180)), D(random.randint(1,180)), 0) for i in range(20)],
    ["id","policy_code","policy_name","policy_type","publish_dept_id","publish_dept_name","publish_date","implement_date",
     "effective_status","content","file_url","keywords","view_count","status","create_time","update_time","deleted"])

table("gov_open", "t_open_apply",
    [(100+i, f"AP-{2024000+i+1:05d}", random.randint(100,149), f"用户{random.randint(100,149)}",
      f"1380000{random.randint(100,149):04d}", f"11010119900101{random.randint(100,149):04d}",
      f"申请内容{i}", random.choice(["生产生活需要","科研需要","自身利益","其他"]),
      random.choice(["信息不存在","涉密","非政府信息",""]) if random.random()>0.7 else None,
      random.choice(["1","2","3"]), random.choice(dept_ids),
      random.randint(100,149) if random.random()>0.5 else None,
      random.choice(["0","1","1","2","3","4"]),
      D(random.randint(1,60)), D(random.randint(1,50)) if random.random()>0.5 else None,
      D(random.randint(1,30)) if random.random()>0.5 else None,
      f"回复内容{i}" if random.random()>0.5 else None,
      random.choice(["全文公开","部分公开","不予公开"]) if random.random()>0.5 else None,
      random.choice([0,0,1]) if random.random()>0.5 else 0,
      random.choice([0,10,20,50]) if random.random()>0.5 else None,
      D(random.randint(1,60)), D(random.randint(1,30)), 1, 1, 0) for i in range(30)],
    ["id","apply_no","user_id","user_name","user_phone","user_id_card","apply_content","apply_reason","reject_reason","apply_type","dept_id","handler_id","status","apply_time","accept_time","reply_time","reply_content","reply_type","fee_flag","fee_amount","create_time","update_time","create_by","update_by","deleted"])

table("gov_open", "t_open_content",
    [(100+i, random.choice(["notice","policy"]), random.randint(100,129) if i<20 else random.randint(100,119),
      random.choice(["1","1","1","0","2","3"]),
      random.randint(100,149) if random.random()>0.5 else None,
      D(random.randint(1,30)) if random.random()>0.5 else None,
      f"审核意见{i}", D(random.randint(1,60)), D(random.randint(1,30)), 1, 1, 0) for i in range(40)],
    ["id","content_type","content_id","audit_status","audit_by","audit_time","audit_opinion","create_time","update_time","create_by","update_by","deleted"])

table("gov_open", "t_open_feedback",
    [(100+i, random.choice(["notice","policy"]), random.randint(100,129) if i<10 else random.randint(100,119),
      random.randint(100,149), f"用户{random.randint(100,149)}",
      random.choice(["好评","差评","建议","纠错","咨询"]), f"反馈内容{i}",
      random.choice(["1","1","0","2","3"]),
      D(random.randint(1,15)) if random.random()>0.5 else None,
      f"处理结果{i}" if random.random()>0.5 else None,
      D(random.randint(1,60)), D(random.randint(1,30)), 1, 1, 0) for i in range(25)],
    ["id","content_type","content_id","user_id","user_name","feedback_type","feedback_content","status","handle_time","handle_result","create_time","update_time","create_by","update_by","deleted"])

# ======================== gov_datashare ========================
table("gov_datashare", "t_share_api",
    [(100+i, f"API-{i+1:03d}", f"数据接口{i+1}", random.randint(1,8),
      f"/api/share/query{i+1}", random.choice(["GET","POST"]), "{}", "{}",
      f"描述{i}", random.choice(["TOKEN","NONE"]), random.choice([3000,5000,10000]),
      random.choice([50,100,200]), random.choice(dept_ids), f"部门{random.choice(dept_ids)}",
      random.choice(["1","1","1","0"]), D(random.randint(1,60)), D(random.randint(1,60)), 0) for i in range(15)],
    ["id","api_code","api_name","source_id","api_url","api_method","request_params","response_params","api_desc","auth_type","timeout","rate_limit","dept_id","dept_name","status","create_time","update_time","deleted"])

table("gov_datashare", "t_datasource",
    [(100+i, f"DS-{i+1:03d}", f"数据源{i+1}", random.choice(["MySQL","Oracle","API","FILE"]),
      f"192.168.{i+100}.{i+1}", random.choice([3306,3307,1521]),
      f"db_source_{i}", f"user_{i}", f"pass_{i}",
      f"http://api{i}.gov.cn/data" if i%2==0 else None, "GET" if i%2==0 else None, f"key_{i}" if i%2==0 else None,
      random.choice(dept_ids), f"部门{random.choice(dept_ids)}",
      random.choice(["1","1","1","0"]), D(random.randint(1,90)), D(random.randint(1,30)), 1, 1, 0) for i in range(8)],
    ["id","source_code","source_name","source_type","db_host","db_port","db_name","db_username","db_password","api_url","api_method","api_key","dept_id","dept_name","status","create_time","update_time","create_by","update_by","deleted"])

table("gov_datashare", "t_share_log",
    [(100+i, 100+random.randint(0,14), f"API-{random.randint(1,15):03d}",
      random.choice(dept_ids), f"部门{random.choice(dept_ids)}",
      random.randint(100,149), D(random.randint(1,30)),
      f'{{"page":1,"size":10}}', random.choice(["1","1","1","2",""]),
      "调用失败" if random.random()<0.1 else None, random.randint(20,3000), random.randint(1,100),
      f"192.168.{random.randint(1,255)}.{random.randint(1,255)}",
      D(random.randint(1,30)), D(random.randint(1,30)), 1, 1, 0) for i in range(50)],
    ["id","api_id","api_code","caller_dept_id","caller_dept_name","caller_user_id","call_time","call_params","call_result","call_msg","response_time","data_count","call_ip","create_time","update_time","create_by","update_by","deleted"])

table("gov_datashare", "t_share_permission",
    [(100+i, 100+random.randint(0,14), random.choice(dept_ids), f"部门{random.choice(dept_ids)}",
      random.choice(["读写","只读","完全"]),
      EX(random.randint(30,365)) if random.random()>0.3 else None,
      random.choice([100,500,1000,5000,10000]), random.choice(["1","1","1","0","2"]),
      D(random.randint(1,90)), D(random.randint(1,30)), 1, 1, 0) for i in range(20)],
    ["id","api_id","dept_id","dept_name","permission_type","expire_time","call_limit","status","create_time","update_time","create_by","update_by","deleted"])

table("gov_datashare", "t_share_subscribe",
    [(100+i, 100+random.randint(0,14), f"API-{random.randint(1,15):03d}",
      random.choice(dept_ids), f"部门{random.choice(dept_ids)}",
      random.randint(100,149), f"用户{random.randint(100,149)}",
      D(random.randint(1,60)), random.choice(["实时","定时","手动"]),
      random.choice([300,600,1800,3600,86400]),
      D(random.randint(1,10)) if random.random()>0.3 else None,
      random.choice(["1","1","1","0","2"]),
      D(random.randint(1,60)), D(random.randint(1,30)), 1, 1, 0) for i in range(20)],
    ["id","api_id","api_name","subscribe_dept_id","subscribe_dept_name","subscribe_user_id","subscribe_user_name","subscribe_time","subscribe_type","sync_interval","last_sync_time","status","create_time","update_time","create_by","update_by","deleted"])

# ======================== gov_message ========================
table("gov_message", "t_message_template",
    [(100,"TPL-001","待办通知","待办【${itemName}】","NOTIFY","SMS,EMAIL,SITE","1",'["itemName"]',1,D(30),D(30),0),
     (101,"TPL-002","办结通知","办件【${applyNo}】证照【${licenseNo}】","NOTIFY","SMS,SITE","1",'["applyNo","licenseNo"]',1,D(30),D(30),0),
     (102,"TPL-003","驳回通知","驳回【${reason}】","NOTIFY","SMS,SITE","1",'["reason"]',1,D(30),D(30),0),
     (103,"TPL-004","验证码","验证码【${code}】","VERIFY","SMS","1",'["code"]',1,D(30),D(30),0),
     (104,"TPL-005","系统公告","公告:${content}","BROADCAST","SITE","1",'["content"]',1,D(30),D(30),0),
     (105,"TPL-006","到期提醒","证照【${licenseNo}】即将到期","REMIND","SMS,EMAIL","1",'["licenseNo"]',1,D(30),D(30),0),
     (106,"TPL-007","审批提醒","审批【${itemName}】剩余${hours}时","REMIND","SMS,SITE","1",'["itemName","hours"]',1,D(30),D(30),0)],
    ["id","template_code","template_name","template_content","template_type","channel","template_html","variables","status","create_time","update_time","deleted"])

table("gov_message", "t_message_record",
    [(100+i, 100+i%7, f"TPL-{i%7+1:03d}", random.randint(100,149), f"用户{random.randint(100,149)}",
      f"1380000{random.randint(100,149):04d}", f"user{random.randint(100,149)}@test.com",
      f"消息内容{i}", random.choice(["SMS","EMAIL","SITE"]), "TEST", f"BIZ-{i}",
      D(random.randint(0,10)), random.choice(["1","1","1","1","0","2"]),
      "发送失败" if random.random()<0.1 else None, random.randint(1,3) if random.random()<0.1 else 0,
      D(random.randint(0,30)), D(random.randint(0,30)), 0) for i in range(50)],
    ["id","template_id","template_code","receiver_id","receiver_name","receiver_phone","receiver_email","content","channel","business_type","business_id","send_time","send_status","send_msg","retry_count","create_time","update_time","deleted"])

table("gov_message", "t_message_inbox",
    [(100+i, random.randint(100,149), f"站内信标题{i}", f"站内信内容{i}", "NOTIFY","TEST",f"BIZ-{i}",
      1 if random.random()>0.3 else 0,
      D(random.randint(1,30)) if random.random()>0.3 else None,
      D(random.randint(0,10)), EX(random.randint(7,365)),
      D(random.randint(0,30)), 0) for i in range(30)],
    ["id","user_id","title","content","msg_type","business_type","business_id","is_read","read_time","send_time","expire_time","create_time","deleted"])

table("gov_message", "t_message_config",
    [(100,"SMS","短信通道","accessKey","LTAI5tABCD1234","key","aliyun","1",D(365),D(365),1,1,0),
     (101,"SMS","短信通道","signName","智慧政务","key","aliyun","1",D(365),D(365),1,1,0),
     (102,"EMAIL","邮件通道","smtpHost","smtp.gov.cn","server","internal","1",D(365),D(365),1,1,0),
     (103,"EMAIL","邮件通道","smtpPort","465","server","internal","1",D(365),D(365),1,1,0),
     (104,"SITE","站内信通道","maxUnread","100","limit","internal","1",D(365),D(365),1,1,0),
     (105,"SMS","短信通道","retryMax","3","policy","aliyun","1",D(365),D(365),1,1,0),
     (106,"ALL","全局","defaultChannel","SITE","default","internal","1",D(365),D(365),1,1,0)],
    ["id","channel","channel_name","config_key","config_value","config_type","provider","status","create_time","update_time","create_by","update_by","deleted"])

table("gov_message", "t_message_queue",
    [(100+i, 100+random.randint(0,49), random.choice(["1","2","1","1","3","4","5"]),
      random.randint(1,5), EX(random.randint(-1,10)) if random.random()>0.3 else None,
      D(random.randint(1,30)) if random.random()>0.3 else None,
      random.randint(0,3) if random.random()>0.7 else 0, 3,
      "发送超时" if random.random()<0.1 else None,
      D(random.randint(1,30)), D(random.randint(1,30)), 1, 1, 0) for i in range(30)],
    ["id","record_id","queue_status","priority","scheduled_time","process_time","retry_count","max_retry","error_msg","create_time","update_time","create_by","update_by","deleted"])

# ======================== gov_monitor ========================
table("gov_monitor", "t_operate_log",
    [(100+i, random.randint(100,149), f"用户{random.randint(100,149)}", random.choice(dept_ids), f"部门{random.choice(dept_ids)}",
      random.choice(["用户管理","事项管理","窗口受理","审批管理","证照管理","系统管理","数据管理","消息管理","投诉管理","政务公开"]),
      random.choice(["新增","修改","删除","查询","发布","下架","审批","驳回","导出","导入"]),
      random.choice(["GET","POST","PUT","DELETE"]), f"/api/{i}/{random.randint(1,100)}",
      random.choice(["GET","POST","PUT","DELETE"]),
      "{}", "{}", D(random.randint(0,10)),
      f"192.168.{random.randint(1,255)}.{random.randint(1,255)}",
      random.choice(["本地","北京","上海","广州"]),
      random.randint(10,500), random.choice(["1","1","1","1","0"]),
      "异常" if random.random()<0.05 else None,
      D(random.randint(0,30)), D(random.randint(0,30)), 1, 1, 0) for i in range(80)],
    ["id","user_id","user_name","dept_id","dept_name","module","action","method","request_url","request_type","request_params","response_data","operate_time","operate_ip","operate_location","execute_time","status","error_msg","create_time","update_time","create_by","update_by","deleted"])

table("gov_monitor", "t_dashboard_stat",
    [(100+i, s, str(v), DT(0), str(NOW.hour+1), 0, D(0), 0) for i,(s,v) in enumerate([
        ("TOTAL_ITEM",149),("TOTAL_USER",149),("TOTAL_RECORD",500),("TOTAL_LICENSE",130),
        ("TOTAL_COMPLAINT",150),("TODAY_RECORD",23),("TODAY_COMPLAINT",5),("PENDING_TASK",45),
        ("FINISH_RATE",87.5),("SATISFACTION_RATE",92.3),("TIMEOUT_RATE",3.2),
    ])],
    ["id","stat_type","stat_value","stat_date","stat_hour","dept_id","create_time","deleted"])

table("gov_monitor", "t_audit_report",
    [(100+i, f"AR-{2024000+i+1:05d}", f"审计报告{i+1}", random.randint(1,3), DT(random.randint(1,90)),
      f"审计内容{i}", f"/files/audit_{i}.pdf", D(random.randint(1,90)), random.randint(100,149),
      random.choice([1,1,0]), D(random.randint(1,90)), 0) for i in range(15)],
    ["id","report_no","report_name","report_type","report_date","report_content","file_url","generate_time","generate_by","status","create_time","deleted"])

table("gov_monitor", "t_efficiency_stat",
    [(100+i, dept_ids[i%10], f"部门{dept_ids[i%10]}", DT(random.randint(1,30)),
      random.randint(10,200), random.randint(5,150), random.randint(0,10), random.randint(0,5),
      random.randint(1,72), random.randint(50,240), random.randint(1,20),
      round(random.uniform(3.5,5.0),2), random.randint(0,5), random.randint(0,2),
      D(random.randint(1,30)), 0) for i in range(30)],
    ["id","dept_id","dept_name","stat_date","total_count","finish_count","timeout_count","reject_count","avg_time","max_time","min_time","satisfaction_avg","yellow_count","red_count","create_time","deleted"])

table("gov_monitor", "t_login_log_monitor",
    [(100+i, random.randint(100,149), f"user{random.randint(100,149):03d}",
      f"192.168.{random.randint(1,255)}.{random.randint(1,255)}",
      random.choice(["本地","北京","上海","广州","深圳"]),
      D(random.randint(0,30)), random.choice([1,2,3]), random.choice([1,1,1,1,0]),
      "密码错误" if random.random()<0.08 else None,
      random.choice(["Chrome","Firefox","Edge","Safari"]),
      random.choice(["Windows","macOS","Linux","iOS","Android"]),
      D(random.randint(1,30)), 0) for i in range(50)],
    ["id","user_id","username","login_ip","login_location","login_time","login_type","login_status","login_msg","browser","os","create_time","deleted"])

table("gov_monitor", "t_satisfaction_stat",
    [(100+i, dept_ids[i%10], f"部门{dept_ids[i%10]}", DT(random.randint(1,30)),
      random.randint(20,200), random.randint(10,100), random.randint(0,20), random.randint(0,5),
      random.randint(30,325), round(random.uniform(75.0,99.9),2),
      D(random.randint(1,30)), 0) for i in range(30)],
    ["id","dept_id","dept_name","stat_date","very_satisfied","satisfied","neutral","unsatisfied","total_count","satisfaction_rate","create_time","deleted"])

table("gov_monitor", "t_warning_record",
    [(100+i, f"BJ-{2024000+1+i:05d}", 100+random.randint(0,49), item_names[random.randint(0,49)],
      random.choice(dept_ids), f"部门{random.choice(dept_ids)}",
      random.choice(["超时","临期","质量","依法"]), random.choice([1,2,3,3,2,1]),
      D(random.randint(1,60)), f"预警原因{i}", random.randint(1,48),
      random.choice([0,0,0,1,2,2]),
      D(random.randint(1,50)) if random.random()>0.5 else None,
      random.randint(100,149) if random.random()>0.5 else None,
      f"已处理{i}" if random.random()>0.5 else None,
      D(random.randint(1,60)), 0) for i in range(25)],
    ["id","apply_no","item_id","item_name","dept_id","dept_name","warning_type","warning_level","warning_time","warning_reason","remaining_time","handle_status","handle_time","handle_by","handle_remark","create_time","deleted"])

# ======================== 收尾 ========================
out.append("SET FOREIGN_KEY_CHECKS = 1;")
path = os.path.join(os.path.dirname(os.path.dirname(__file__)), "docker/mysql/init/98-test-seed-data.sql")
os.makedirs(os.path.dirname(path), exist_ok=True)
with open(path, "w") as f:
    f.write("\n".join(out))
total = sum(1 for l in out if l.startswith("INSERT INTO"))
print(f"Done: {path}")
print(f"  {len(out)} lines, {total} INSERTs, {sum(len(v) for v in tables.values())} tables total")
