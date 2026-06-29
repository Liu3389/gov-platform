SET NAMES utf8mb4;
-- =====================================================
-- 智慧政务一体化便民服务平台 - 建表脚本
-- 事项管理库（gov_item）— 组员B
-- =====================================================

USE gov_item;

-- 事项基本信息表
CREATE TABLE IF NOT EXISTS `t_item_base` (
  `id` BIGINT NOT NULL COMMENT '事项ID（雪花ID）',
  `item_code` VARCHAR(50) NOT NULL COMMENT '事项编码（唯一）',
  `item_name` VARCHAR(100) NOT NULL COMMENT '事项名称',
  `dept_id` BIGINT NOT NULL COMMENT '所属部门ID',
  `category_id` BIGINT COMMENT '事项分类ID',
  `item_type` TINYINT DEFAULT 1 COMMENT '事项类型：1行政许可 2公共服务 3行政确认 4行政奖励 5行政给付',
  `item_level` TINYINT DEFAULT 1 COMMENT '事项层级：1省级 2市级 3县级 4乡镇级',
  `implement_type` TINYINT DEFAULT 1 COMMENT '实施类型：1即办 2承诺办',
  `time_limit` INT DEFAULT 0 COMMENT '办理时限（工作日）',
  `fee_flag` TINYINT DEFAULT 0 COMMENT '是否收费：0不收费 1收费',
  `fee_standard` VARCHAR(200) COMMENT '收费标准',
  `online_flag` TINYINT DEFAULT 1 COMMENT '是否支持网上办理：0否 1是',
  `window_flag` TINYINT DEFAULT 1 COMMENT '是否支持窗口办理：0否 1是',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0草稿 1发布 2下架',
  `version` INT DEFAULT 1 COMMENT '版本号',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT COMMENT '创建人ID',
  `update_by` BIGINT COMMENT '更新人ID',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_item_code` (`item_code`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_status` (`status`),
  KEY `idx_item_type` (`item_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='政务服务事项基本信息';

-- 事项版本表
CREATE TABLE IF NOT EXISTS `t_item_version` (
  `id` BIGINT NOT NULL COMMENT '版本ID（雪花ID）',
  `item_id` BIGINT NOT NULL COMMENT '事项ID',
  `version_no` INT NOT NULL COMMENT '版本号',
  `version_name` VARCHAR(50) COMMENT '版本名称',
  `publish_time` DATETIME COMMENT '发布时间',
  `publish_by` BIGINT COMMENT '发布人ID',
  `status` TINYINT DEFAULT 1 COMMENT '状态：1当前版本 0历史版本',
  `change_log` TEXT COMMENT '变更日志',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  KEY `idx_item_id` (`item_id`),
  KEY `idx_version_no` (`version_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='事项版本表';

-- 事项分类表
CREATE TABLE IF NOT EXISTS `t_item_category` (
  `id` BIGINT NOT NULL COMMENT '分类ID（雪花ID）',
  `category_name` VARCHAR(50) NOT NULL COMMENT '分类名称',
  `category_code` VARCHAR(50) NOT NULL COMMENT '分类编码（唯一）',
  `parent_id` BIGINT DEFAULT 0 COMMENT '上级分类ID',
  `category_level` TINYINT DEFAULT 1 COMMENT '分类层级：1一级 2二级 3三级',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT DEFAULT 1 COMMENT '状态：1正常 0禁用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT COMMENT '创建人ID',
  `update_by` BIGINT COMMENT '更新人ID',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_category_code` (`category_code`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='事项分类表';

-- 办事指南表
CREATE TABLE IF NOT EXISTS `t_item_guide` (
  `id` BIGINT NOT NULL COMMENT '指南ID（雪花ID）',
  `item_id` BIGINT NOT NULL COMMENT '事项ID',
  `guide_content` TEXT COMMENT '办理指南内容',
  `accept_condition` TEXT COMMENT '受理条件',
  `reject_condition` TEXT COMMENT '不予受理条件',
  `time_limit` INT COMMENT '办理时限（工作日）',
  `fee_info` VARCHAR(200) COMMENT '收费信息',
  `result_type` VARCHAR(50) COMMENT '结果类型（证照名称）',
  `process_desc` TEXT COMMENT '办理流程描述',
  `consult_phone` VARCHAR(50) COMMENT '咨询电话',
  `consult_address` VARCHAR(200) COMMENT '咨询地址',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  KEY `idx_item_id` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='办事指南表';

-- 材料清单表
CREATE TABLE IF NOT EXISTS `t_item_material` (
  `id` BIGINT NOT NULL COMMENT '材料ID（雪花ID）',
  `item_id` BIGINT NOT NULL COMMENT '事项ID',
  `material_name` VARCHAR(100) NOT NULL COMMENT '材料名称',
  `material_code` VARCHAR(50) COMMENT '材料编码',
  `material_type` TINYINT DEFAULT 1 COMMENT '材料类型：1原件 2复印件 3电子件',
  `is_required` TINYINT DEFAULT 1 COMMENT '是否必须：1必须 0可选',
  `source_type` TINYINT DEFAULT 1 COMMENT '来源类型：1申请人提供 2政府部门核验',
  `sample_url` VARCHAR(200) COMMENT '样例下载地址',
  `template_url` VARCHAR(200) COMMENT '模板下载地址',
  `remark` VARCHAR(200) COMMENT '备注',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  KEY `idx_item_id` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='材料清单表';

-- 表单配置表
CREATE TABLE IF NOT EXISTS `t_item_form` (
  `id` BIGINT NOT NULL COMMENT '表单ID（雪花ID）',
  `item_id` BIGINT NOT NULL COMMENT '事项ID',
  `form_name` VARCHAR(50) COMMENT '表单名称',
  `form_schema` TEXT COMMENT '表单结构（JSON格式）',
  `form_type` TINYINT DEFAULT 1 COMMENT '表单类型：1申请表 2承诺书 3其他',
  `is_required` TINYINT DEFAULT 1 COMMENT '是否必须填写',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  KEY `idx_item_id` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='表单配置表';

-- 事项流程关联表
CREATE TABLE IF NOT EXISTS `t_item_process` (
  `id` BIGINT NOT NULL COMMENT '关联ID（雪花ID）',
  `item_id` BIGINT NOT NULL COMMENT '事项ID',
  `process_key` VARCHAR(50) NOT NULL COMMENT '流程Key（Activiti）',
  `process_name` VARCHAR(100) COMMENT '流程名称',
  `is_default` TINYINT DEFAULT 1 COMMENT '是否默认流程：1是 0否',
  `status` TINYINT DEFAULT 1 COMMENT '状态：1启用 0禁用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  KEY `idx_item_id` (`item_id`),
  KEY `idx_process_key` (`process_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='事项流程关联表';

-- 事项部门关联表
CREATE TABLE IF NOT EXISTS `t_item_dept` (
  `id` BIGINT NOT NULL COMMENT '关联ID（雪花ID）',
  `item_id` BIGINT NOT NULL COMMENT '事项ID',
  `dept_id` BIGINT NOT NULL COMMENT '部门ID',
  `dept_type` TINYINT DEFAULT 1 COMMENT '部门类型：1主办 2协办',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  KEY `idx_item_id` (`item_id`),
  KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='事项部门关联表';