-- =====================================================
-- 智慧政务一体化便民服务平台 - 建表脚本
-- 监察审计库（gov_monitor）— 组长维护
-- =====================================================

USE gov_monitor;

-- 操作日志表
CREATE TABLE IF NOT EXISTS `t_operate_log` (
  `id` BIGINT NOT NULL COMMENT '日志ID（雪花ID）',
  `user_id` BIGINT COMMENT '操作人ID',
  `user_name` VARCHAR(50) COMMENT '操作人姓名',
  `dept_id` BIGINT COMMENT '操作部门ID',
  `dept_name` VARCHAR(50) COMMENT '操作部门名称',
  `module` VARCHAR(50) COMMENT '操作模块',
  `action` VARCHAR(50) COMMENT '操作动作',
  `method` VARCHAR(200) COMMENT '请求方法',
  `request_url` VARCHAR(200) COMMENT '请求URL',
  `request_type` VARCHAR(10) COMMENT '请求类型：GET/POST/PUT/DELETE',
  `request_params` TEXT COMMENT '请求参数',
  `response_data` TEXT COMMENT '响应数据',
  `operate_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `operate_ip` VARCHAR(50) COMMENT '操作IP',
  `operate_location` VARCHAR(100) COMMENT '操作地点',
  `execute_time` INT COMMENT '执行时间（毫秒）',
  `status` TINYINT DEFAULT 1 COMMENT '状态：1成功 0失败',
  `error_msg` VARCHAR(500) COMMENT '错误消息',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_operate_time` (`operate_time`),
  KEY `idx_module` (`module`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- 登录日志表
CREATE TABLE IF NOT EXISTS `t_login_log_monitor` (
  `id` BIGINT NOT NULL COMMENT '日志ID（雪花ID）',
  `user_id` BIGINT COMMENT '用户ID',
  `username` VARCHAR(50) COMMENT '登录用户名',
  `login_ip` VARCHAR(50) COMMENT '登录IP',
  `login_location` VARCHAR(100) COMMENT '登录地点',
  `login_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  `login_type` TINYINT DEFAULT 1 COMMENT '登录类型：1PC 2移动端 3窗口终端',
  `login_status` TINYINT DEFAULT 1 COMMENT '登录状态：1成功 0失败',
  `login_msg` VARCHAR(200) COMMENT '登录消息',
  `browser` VARCHAR(50) COMMENT '浏览器',
  `os` VARCHAR(50) COMMENT '操作系统',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_login_time` (`login_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录日志表（监控端）';

-- 效能统计表
CREATE TABLE IF NOT EXISTS `t_efficiency_stat` (
  `id` BIGINT NOT NULL COMMENT '统计ID（雪花ID）',
  `dept_id` BIGINT NOT NULL COMMENT '部门ID',
  `dept_name` VARCHAR(50) COMMENT '部门名称',
  `stat_date` DATE NOT NULL COMMENT '统计日期',
  `total_count` INT DEFAULT 0 COMMENT '总办件数',
  `finish_count` INT DEFAULT 0 COMMENT '完成办件数',
  `timeout_count` INT DEFAULT 0 COMMENT '超期办件数',
  `reject_count` INT DEFAULT 0 COMMENT '驳回办件数',
  `avg_time` INT COMMENT '平均办理时长（分钟）',
  `max_time` INT COMMENT '最长办理时长（分钟）',
  `min_time` INT COMMENT '最短办理时长（分钟）',
  `satisfaction_avg` DECIMAL(3,2) COMMENT '平均满意度',
  `yellow_count` INT DEFAULT 0 COMMENT '黄牌数量',
  `red_count` INT DEFAULT 0 COMMENT '红牌数量',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_stat_date` (`stat_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='效能统计表';

-- 红黄牌预警表
CREATE TABLE IF NOT EXISTS `t_warning_record` (
  `id` BIGINT NOT NULL COMMENT '预警ID（雪花ID）',
  `apply_no` VARCHAR(20) NOT NULL COMMENT '办件号',
  `item_id` BIGINT COMMENT '事项ID',
  `item_name` VARCHAR(100) COMMENT '事项名称',
  `dept_id` BIGINT COMMENT '部门ID',
  `dept_name` VARCHAR(50) COMMENT '部门名称',
  `warning_type` VARCHAR(20) NOT NULL COMMENT '预警类型：YELLOW_CARD/RED_CARD',
  `warning_level` TINYINT DEFAULT 1 COMMENT '预警级别：1黄牌 2红牌',
  `warning_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '预警时间',
  `warning_reason` VARCHAR(500) COMMENT '预警原因',
  `remaining_time` INT COMMENT '剩余时间（分钟）',
  `handle_status` TINYINT DEFAULT 0 COMMENT '处理状态：0待处理 1已处理',
  `handle_time` DATETIME COMMENT '处理时间',
  `handle_by` BIGINT COMMENT '处理人ID',
  `handle_remark` VARCHAR(200) COMMENT '处理备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  KEY `idx_apply_no` (`apply_no`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_warning_type` (`warning_type`),
  KEY `idx_warning_time` (`warning_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='红黄牌预警表';

-- 满意度统计表
CREATE TABLE IF NOT EXISTS `t_satisfaction_stat` (
  `id` BIGINT NOT NULL COMMENT '统计ID（雪花ID）',
  `dept_id` BIGINT COMMENT '部门ID',
  `dept_name` VARCHAR(50) COMMENT '部门名称',
  `stat_date` DATE NOT NULL COMMENT '统计日期',
  `very_satisfied` INT DEFAULT 0 COMMENT '非常满意数量',
  `satisfied` INT DEFAULT 0 COMMENT '满意数量',
  `neutral` INT DEFAULT 0 COMMENT '一般数量',
  `unsatisfied` INT DEFAULT 0 COMMENT '不满意数量',
  `total_count` INT DEFAULT 0 COMMENT '总评价数',
  `satisfaction_rate` DECIMAL(5,2) COMMENT '满意率（%）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_stat_date` (`stat_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='满意度统计表';

-- 审计报告表
CREATE TABLE IF NOT EXISTS `t_audit_report` (
  `id` BIGINT NOT NULL COMMENT '报告ID（雪花ID）',
  `report_no` VARCHAR(50) NOT NULL COMMENT '报告编号',
  `report_name` VARCHAR(100) NOT NULL COMMENT '报告名称',
  `report_type` TINYINT DEFAULT 1 COMMENT '报告类型：1日报 2周报 3月报 4年报',
  `report_date` DATE COMMENT '报告日期',
  `report_content` TEXT COMMENT '报告内容（JSON格式）',
  `file_url` VARCHAR(200) COMMENT '报告文件URL',
  `generate_time` DATETIME COMMENT '生成时间',
  `generate_by` BIGINT COMMENT '生成人ID',
  `status` TINYINT DEFAULT 1 COMMENT '状态：1已生成 0待生成',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_report_no` (`report_no`),
  KEY `idx_report_type` (`report_type`),
  KEY `idx_report_date` (`report_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='审计报告表';

-- 数据大屏统计表
CREATE TABLE IF NOT EXISTS `t_dashboard_stat` (
  `id` BIGINT NOT NULL COMMENT '统计ID（雪花ID）',
  `stat_type` VARCHAR(50) NOT NULL COMMENT '统计类型：total/finish/timeout/satisfaction',
  `stat_value` BIGINT COMMENT '统计值',
  `stat_date` DATE COMMENT '统计日期',
  `stat_hour` TINYINT COMMENT '统计小时（实时统计）',
  `dept_id` BIGINT COMMENT '部门ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  KEY `idx_stat_type` (`stat_type`),
  KEY `idx_stat_date` (`stat_date`),
  KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据大屏统计表';