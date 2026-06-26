-- ============================================================
-- gov_reception 数据库 - 统一受理服务
-- 创建人：成员B
-- 创建时间：2024
-- ============================================================

USE `gov_reception`;

-- -----------------------------------------------------------
-- 1. 办件主表 t_reception_record
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_reception_record` (
  `id` BIGINT NOT NULL COMMENT '主键（雪花ID）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT COMMENT '创建人ID',
  `update_by` BIGINT COMMENT '更新人ID',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',

  `apply_no` VARCHAR(50) NOT NULL COMMENT '办件号（年份+部门编码+序号）',
  `item_id` BIGINT NOT NULL COMMENT '事项ID',
  `user_id` BIGINT NOT NULL COMMENT '申请人ID',
  `dept_id` BIGINT NOT NULL COMMENT '受理部门ID',
  `window_id` BIGINT COMMENT '受理窗口ID',
  `operator_id` BIGINT COMMENT '受理人ID',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0待受理 1受理中 2审批中 3办结 4驳回',
  `accept_time` DATETIME COMMENT '受理时间',
  `finish_time` DATETIME COMMENT '办结时间',
  `process_instance_id` VARCHAR(100) COMMENT '流程实例ID',
  `remark` VARCHAR(500) COMMENT '备注',
  `reject_reason` VARCHAR(500) COMMENT '驳回原因',

  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_apply_no` (`apply_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_status` (`status`),
  KEY `idx_item_id` (`item_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='办件主表';

-- -----------------------------------------------------------
-- 2. 申报材料表 t_reception_material
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_reception_material` (
  `id` BIGINT NOT NULL COMMENT '主键（雪花ID）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT COMMENT '创建人ID',
  `update_by` BIGINT COMMENT '更新人ID',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',

  `record_id` BIGINT NOT NULL COMMENT '办件ID',
  `material_name` VARCHAR(100) NOT NULL COMMENT '材料名称',
  `material_type` VARCHAR(50) COMMENT '材料类型（身份证/户口本/营业执照等）',
  `file_url` VARCHAR(500) COMMENT '文件存储路径',
  `file_size` BIGINT COMMENT '文件大小（字节）',
  `file_type` VARCHAR(20) COMMENT '文件类型（jpg/png/pdf）',
  `is_required` TINYINT DEFAULT 1 COMMENT '是否必须：0否 1是',
  `verify_status` TINYINT DEFAULT 0 COMMENT '审核状态：0待审核 1合格 2不合格 3缺失',
  `verify_remark` VARCHAR(200) COMMENT '审核备注',
  `verify_time` DATETIME COMMENT '审核时间',
  `verify_by` BIGINT COMMENT '审核人ID',

  PRIMARY KEY (`id`),
  KEY `idx_record_id` (`record_id`),
  KEY `idx_verify_status` (`verify_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='申报材料表';

-- -----------------------------------------------------------
-- 3. 表单数据表 t_reception_form_data
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_reception_form_data` (
  `id` BIGINT NOT NULL COMMENT '主键（雪花ID）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT COMMENT '创建人ID',
  `update_by` BIGINT COMMENT '更新人ID',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',

  `record_id` BIGINT NOT NULL COMMENT '办件ID',
  `form_data` TEXT COMMENT '表单数据（JSON格式）',
  `form_version` INT DEFAULT 1 COMMENT '表单版本号',

  PRIMARY KEY (`id`),
  KEY `idx_record_id` (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='表单数据表';

-- -----------------------------------------------------------
-- 4. 办件日志表 t_reception_log
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_reception_log` (
  `id` BIGINT NOT NULL COMMENT '主键（雪花ID）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT COMMENT '创建人ID',
  `update_by` BIGINT COMMENT '更新人ID',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',

  `record_id` BIGINT NOT NULL COMMENT '办件ID',
  `action_type` TINYINT NOT NULL COMMENT '操作类型：1提交 2受理 3审批 4驳回 5办结 6出件',
  `action_desc` VARCHAR(200) COMMENT '操作描述',
  `operator_id` BIGINT COMMENT '操作人ID',
  `operator_name` VARCHAR(50) COMMENT '操作人姓名',
  `operate_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `remark` VARCHAR(500) COMMENT '备注',

  PRIMARY KEY (`id`),
  KEY `idx_record_id` (`record_id`),
  KEY `idx_action_type` (`action_type`),
  KEY `idx_operate_time` (`operate_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='办件日志表';

-- -----------------------------------------------------------
-- 5. 出件记录表 t_reception_output
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_reception_output` (
  `id` BIGINT NOT NULL COMMENT '主键（雪花ID）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT COMMENT '创建人ID',
  `update_by` BIGINT COMMENT '更新人ID',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',

  `record_id` BIGINT NOT NULL COMMENT '办件ID',
  `output_type` TINYINT DEFAULT 1 COMMENT '出件类型：1证照 2批文 3回执',
  `license_id` BIGINT COMMENT '证照ID（关联gov_license库）',
  `output_no` VARCHAR(100) COMMENT '出件编号',
  `output_name` VARCHAR(200) COMMENT '出件名称',
  `file_url` VARCHAR(500) COMMENT '文件路径',
  `receiver_id` BIGINT COMMENT '领取人ID',
  `receive_time` DATETIME COMMENT '领取时间',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0待领取 1已领取 2已邮寄',

  PRIMARY KEY (`id`),
  KEY `idx_record_id` (`record_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='出件记录表';

-- -----------------------------------------------------------
-- 6. 窗口信息表 t_reception_window
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_reception_window` (
  `id` BIGINT NOT NULL COMMENT '主键（雪花ID）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT COMMENT '创建人ID',
  `update_by` BIGINT COMMENT '更新人ID',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',

  `window_no` VARCHAR(20) NOT NULL COMMENT '窗口编号',
  `window_name` VARCHAR(50) NOT NULL COMMENT '窗口名称',
  `dept_id` BIGINT COMMENT '所属部门ID',
  `staff_id` BIGINT COMMENT '窗口工作人员ID',
  `service_type` VARCHAR(100) COMMENT '可办理业务类型',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0关闭 1开放 2暂停',
  `sort` INT DEFAULT 0 COMMENT '排序',

  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_window_no` (`window_no`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_staff_id` (`staff_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='窗口信息表';

-- -----------------------------------------------------------
-- 7. 排队叫号表 t_reception_queue
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_reception_queue` (
  `id` BIGINT NOT NULL COMMENT '主键（雪花ID）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT COMMENT '创建人ID',
  `update_by` BIGINT COMMENT '更新人ID',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',

  `queue_no` VARCHAR(20) NOT NULL COMMENT '排队号码',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `window_id` BIGINT COMMENT '窗口ID',
  `item_id` BIGINT COMMENT '事项ID',
  `queue_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '排队时间',
  `call_time` DATETIME COMMENT '叫号时间',
  `finish_time` DATETIME COMMENT '完成时间',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0等待中 1办理中 2已完成 3已取消',
  `priority` INT DEFAULT 0 COMMENT '优先级（数字越大优先级越高）',

  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_queue_no` (`queue_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_window_id` (`window_id`),
  KEY `idx_status` (`status`),
  KEY `idx_queue_time` (`queue_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='排队叫号表';
