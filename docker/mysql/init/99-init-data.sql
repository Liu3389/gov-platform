-- =====================================================
-- 智慧政务一体化便民服务平台 - 初始化数据脚本
-- 插入系统字典、默认配置、行政区划等基础数据
-- =====================================================

USE gov_user;

-- 初始化管理员用户（密码：admin123，BCrypt加密）
INSERT INTO t_user_info (id, username, password, phone, real_name, status, create_time) VALUES
(1, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '13800138000', '系统管理员', 0, NOW());

-- 初始化角色
INSERT INTO t_role_info (id, role_name, role_code, status, remark, create_time) VALUES
(1, '超级管理员', 'admin', 1, '系统超级管理员', NOW()),
(2, '窗口工作人员', 'window_staff', 1, '窗口受理人员', NOW()),
(3, '部门审批人', 'dept_approver', 1, '部门审批人员', NOW()),
(4, '普通用户', 'user', 1, '普通办事群众', NOW());

-- 初始化部门
INSERT INTO t_dept_info (id, dept_name, dept_code, parent_id, dept_type, phone, address, sort, status, create_time) VALUES
(10001, '政务服务中心', 'GOV001', 0, 1, '0512-12345678', '苏州市政务服务中心', 1, 1, NOW()),
(10002, '公安局', 'POL001', 10001, 1, '0512-12345679', '苏州市公安局', 2, 1, NOW()),
(10003, '民政局', 'CIV001', 10001, 1, '0512-12345680', '苏州市民政局', 3, 1, NOW()),
(10004, '人社局', 'HR001', 10001, 1, '0512-12345681', '苏州市人社局', 4, 1, NOW()),
(10005, '卫健委', 'HEA001', 10001, 1, '0512-12345682', '苏州市卫健委', 5, 1, NOW());

-- 初始化菜单
INSERT INTO t_menu (id, menu_name, menu_code, menu_url, menu_type, parent_id, icon, sort, visible, status, create_time) VALUES
(1, '政务服务平台', 'root', '/', 1, 0, 'home', 1, 1, 1, NOW()),
(2, '事项管理', 'item_manage', '/item', 1, 1, 'list', 2, 1, 1, NOW()),
(3, '事项列表', 'item_list', '/item/list', 2, 2, 'table', 1, 1, 1, NOW()),
(4, '事项录入', 'item_add', '/item/add', 2, 2, 'edit', 2, 1, 1, NOW()),
(5, '办件管理', 'case_manage', '/case', 1, 1, 'folder', 3, 1, 1, NOW()),
(6, '待办任务', 'todo_list', '/case/todo', 2, 5, 'todo', 1, 1, 1, NOW()),
(7, '已办任务', 'done_list', '/case/done', 2, 5, 'done', 2, 1, 1, NOW()),
(8, '证照管理', 'license_manage', '/license', 1, 1, 'card', 4, 1, 1, NOW()),
(9, '投诉建议', 'complaint_manage', '/complaint', 1, 1, 'message', 5, 1, 1, NOW()),
(10, '系统管理', 'system_manage', '/system', 1, 1, 'setting', 6, 1, 1, NOW()),
(11, '用户管理', 'user_manage', '/system/user', 2, 10, 'user', 1, 1, 1, NOW()),
(12, '角色管理', 'role_manage', '/system/role', 2, 10, 'role', 2, 1, 1, NOW()),
(13, '部门管理', 'dept_manage', '/system/dept', 2, 10, 'dept', 3, 1, 1, NOW());

-- 初始化数据字典
INSERT INTO t_dict_data (id, dict_type, dict_code, dict_name, dict_value, sort, status, create_time) VALUES
(1, 'item_type', '1', '行政许可', '1', 1, 1, NOW()),
(2, 'item_type', '2', '公共服务', '2', 2, 1, NOW()),
(3, 'item_type', '3', '行政确认', '3', 3, 1, NOW()),
(4, 'item_type', '4', '行政奖励', '4', 4, 1, NOW()),
(5, 'item_type', '5', '行政给付', '5', 5, 1, NOW()),
(10, 'item_level', '1', '省级', '1', 1, 1, NOW()),
(11, 'item_level', '2', '市级', '2', 2, 1, NOW()),
(12, 'item_level', '3', '县级', '3', 3, 1, NOW()),
(13, 'item_level', '4', '乡镇级', '4', 4, 1, NOW()),
(20, 'approval_status', '0', '待受理', '0', 1, 1, NOW()),
(21, 'approval_status', '1', '受理中', '1', 2, 1, NOW()),
(22, 'approval_status', '2', '审批中', '2', 3, 1, NOW()),
(23, 'approval_status', '3', '办结', '3', 4, 1, NOW()),
(24, 'approval_status', '4', '驳回', '4', 5, 1, NOW()),
(30, 'opinion_type', '1', '通过', '1', 1, 1, NOW()),
(31, 'opinion_type', '2', '驳回', '2', 2, 1, NOW()),
(32, 'opinion_type', '3', '转办', '3', 3, 1, NOW()),
(33, 'opinion_type', '4', '退回', '4', 4, 1, NOW()),
(40, 'license_status', '1', '有效', '1', 1, 1, NOW()),
(41, 'license_status', '2', '过期', '2', 2, 1, NOW()),
(42, 'license_status', '3', '注销', '3', 3, 1, NOW()),
(50, 'user_status', '0', '正常', '0', 1, 1, NOW()),
(51, 'user_status', '1', '禁用', '1', 2, 1, NOW()),
(52, 'user_status', '2', '待实名', '2', 3, 1, NOW());

-- 初始化行政区划
INSERT INTO t_region (id, region_code, region_name, parent_code, region_level, sort, status, create_time) VALUES
(1, '320000', '江苏省', NULL, 1, 1, 1, NOW()),
(2, '320500', '苏州市', '320000', 2, 1, 1, NOW()),
(3, '320506', '吴中区', '320500', 3, 1, 1, NOW()),
(4, '320507', '相城区', '320500', 3, 2, 1, NOW()),
(5, '320508', '姑苏区', '320500', 3, 3, 1, NOW()),
(6, '320509', '工业园区', '320500', 3, 4, 1, NOW());

-- 初始化消息模板
USE gov_message;
INSERT INTO t_message_template (id, template_code, template_name, template_content, channel, status, create_time) VALUES
(1, 'TODO_NOTICE', '待办任务通知', '您有新的待办任务：${itemName}，请尽快处理。', 'inbox', 1, NOW()),
(2, 'CASE_FINISH', '办件完成通知', '您的申请已办结，办件号：${applyNo}，证照编号：${licenseNo}。', 'inbox', 1, NOW()),
(3, 'WARNING_YELLOW', '黄牌预警通知', '任务即将超期：${itemName}，剩余时间不足4小时，请尽快处理。', 'inbox', 1, NOW()),
(4, 'WARNING_RED', '红牌预警通知', '任务已超期：${itemName}，请立即处理。', 'inbox', 1, NOW());

-- 初始化证照目录
USE gov_license;
INSERT INTO t_license_catalog (id, catalog_code, catalog_name, dept_id, template_url, status, create_time) VALUES
(1, 'ID_CARD', '身份证', 10002, '/template/id_card.pdf', 1, NOW()),
(2, 'BIRTH_CERT', '出生证明', 10003, '/template/birth_cert.pdf', 1, NOW()),
(3, 'MARRIAGE_CERT', '结婚证', 10003, '/template/marriage_cert.pdf', 1, NOW()),
(4, 'SOCIAL_CARD', '社保卡', 10004, '/template/social_card.pdf', 1, NOW()),
(5, 'HEALTH_CERT', '健康证', 10005, '/template/health_cert.pdf', 1, NOW());
