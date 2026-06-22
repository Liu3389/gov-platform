-- =====================================================
-- 智慧政务一体化便民服务平台 - 建表脚本
-- 事项管理库（gov_item）
-- =====================================================

USE gov_item;

-- 事项基本信息表
CREATE TABLE IF NOT EXISTS `t_item_base` (
  `id` BIGINT NOT NULL COMMENT '事项ID',
  `item_code` VARCHAR(50) NOT NULL COMMENT '事项编码（唯一）',
  `item_name` VARCHAR(100) NOT NULL COMMENT '事项名称',
  `dept_id` BIGINT NOT NULL COMMENT '所属部门ID',
  `item_type` TINYINT DEFAULT 1 COMMENT '事项类型：1行政许可 2公共服务 3行政确认',
  `item_level` TINYINT DEFAULT 1 COMMENT '事项层级：1省级 2市级 3县级',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0草稿 1发布 2下架',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` BIGINT,
  `update_by` BIGINT,
  `deleted` TINYINT DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_item_code` (`item_code`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='政务服务事项基本信息';

-- 事项分类表
CREATE TABLE IF NOT EXISTS `t_item_category` (
  `id` BIGINT NOT NULL COMMENT '分类ID',
  `category_name` VARCHAR(50) NOT NULL COMMENT '分类名称',
  `category_code` VARCHAR(50) NOT NULL COMMENT '分类编码',
  `parent_id` BIGINT DEFAULT 0 COMMENT '上级分类ID',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT DEFAULT 1 COMMENT '状态：1正常 0禁用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` BIGINT,
  `update_by` BIGINT,
  `deleted` TINYINT DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_category_code` (`category_code`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='事项分类表';

-- 办事指南表
CREATE TABLE IF NOT EXISTS `t_item_guide` (
  `id` BIGINT NOT NULL COMMENT '指南ID',
  `item_id` BIGINT NOT NULL COMMENT '事项ID',
  `guide_content` TEXT COMMENT '办理指南内容',
  `time_limit` INT COMMENT '办理时限（工作日）',
  `fee_info` VARCHAR(200) COMMENT '收费信息',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_item_id` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='办事指南表';

-- 材料清单表
CREATE TABLE IF NOT EXISTS `t_item_material` (
  `id` BIGINT NOT NULL COMMENT '材料ID',
  `item_id` BIGINT NOT NULL COMMENT '事项ID',
  `material_name` VARCHAR(100) NOT NULL COMMENT '材料名称',
  `material_type` TINYINT DEFAULT 1 COMMENT '材料类型：1原件 2复印件 3电子件',
  `is_required` TINYINT DEFAULT 1 COMMENT '是否必须：1必须 0可选',
  `sample_url` VARCHAR(200) COMMENT '样例下载地址',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_item_id` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='材料清单表';