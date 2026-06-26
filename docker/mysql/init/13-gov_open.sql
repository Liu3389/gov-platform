-- ============================================================
-- gov_open 数据库 - 政务公开服务
-- 创建人：成员B
-- 创建时间：2024
-- ============================================================

USE `gov_open`;

-- -----------------------------------------------------------
-- 1. 公开目录表 t_open_catalog
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_open_catalog` (
  `id` BIGINT NOT NULL COMMENT '主键（雪花ID）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT COMMENT '创建人ID',
  `update_by` BIGINT COMMENT '更新人ID',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',

  `catalog_code` VARCHAR(50) NOT NULL COMMENT '目录编码',
  `catalog_name` VARCHAR(100) NOT NULL COMMENT '目录名称',
  `parent_id` BIGINT DEFAULT 0 COMMENT '父目录ID（0为顶级）',
  `catalog_type` TINYINT DEFAULT 1 COMMENT '目录类型：1政府信息公开 2政务动态 3重点领域',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0禁用 1启用',

  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_catalog_code` (`catalog_code`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_catalog_type` (`catalog_type`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公开目录表';

-- -----------------------------------------------------------
-- 2. 通知公告表 t_open_notice
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_open_notice` (
  `id` BIGINT NOT NULL COMMENT '主键（雪花ID）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT COMMENT '创建人ID',
  `update_by` BIGINT COMMENT '更新人ID',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',

  `notice_code` VARCHAR(50) NOT NULL COMMENT '公告编号',
  `title` VARCHAR(200) NOT NULL COMMENT '公告标题',
  `content` TEXT COMMENT '公告内容',
  `summary` VARCHAR(500) COMMENT '摘要',
  `publish_dept_id` BIGINT COMMENT '发布部门ID',
  `publish_time` DATETIME COMMENT '发布时间',
  `catalog_id` BIGINT COMMENT '所属目录ID',
  `cover_image` VARCHAR(500) COMMENT '封面图片URL',
  `view_count` INT DEFAULT 0 COMMENT '浏览次数',
  `is_top` TINYINT DEFAULT 0 COMMENT '是否置顶：0否 1是',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0草稿 1已发布 2已撤回',

  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_notice_code` (`notice_code`),
  KEY `idx_publish_dept_id` (`publish_dept_id`),
  KEY `idx_catalog_id` (`catalog_id`),
  KEY `idx_status` (`status`),
  KEY `idx_publish_time` (`publish_time`),
  KEY `idx_is_top` (`is_top`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知公告表';

-- -----------------------------------------------------------
-- 3. 政策法规表 t_open_policy
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_open_policy` (
  `id` BIGINT NOT NULL COMMENT '主键（雪花ID）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT COMMENT '创建人ID',
  `update_by` BIGINT COMMENT '更新人ID',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',

  `policy_code` VARCHAR(50) NOT NULL COMMENT '政策编码',
  `policy_name` VARCHAR(200) NOT NULL COMMENT '政策名称',
  `policy_type` TINYINT DEFAULT 1 COMMENT '政策类型：1法律法规 2部门规章 3规范性文件 4政策解读',
  `content` TEXT COMMENT '政策内容',
  `publish_dept_id` BIGINT COMMENT '发布部门ID',
  `publish_date` DATE COMMENT '发布日期',
  `effective_date` DATE COMMENT '生效日期',
  `expire_date` DATE COMMENT '失效日期',
  `document_no` VARCHAR(100) COMMENT '文号',
  `source` VARCHAR(200) COMMENT '来源',
  `attachment_url` VARCHAR(500) COMMENT '附件URL',
  `view_count` INT DEFAULT 0 COMMENT '浏览次数',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0草稿 1已发布 2已废止',

  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_policy_code` (`policy_code`),
  KEY `idx_policy_type` (`policy_type`),
  KEY `idx_publish_dept_id` (`publish_dept_id`),
  KEY `idx_status` (`status`),
  KEY `idx_publish_date` (`publish_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='政策法规表';

-- -----------------------------------------------------------
-- 4. 依申请公开表 t_open_apply
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_open_apply` (
  `id` BIGINT NOT NULL COMMENT '主键（雪花ID）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT COMMENT '创建人ID',
  `update_by` BIGINT COMMENT '更新人ID',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',

  `apply_no` VARCHAR(50) NOT NULL COMMENT '申请编号',
  `user_id` BIGINT NOT NULL COMMENT '申请人ID',
  `user_name` VARCHAR(50) COMMENT '申请人姓名',
  `user_phone` VARCHAR(20) COMMENT '申请人手机号 [脱敏]',
  `user_id_card` VARCHAR(100) COMMENT '申请人身份证号 [脱敏][SM4加密存储]',
  `apply_title` VARCHAR(200) NOT NULL COMMENT '申请标题',
  `apply_content` TEXT COMMENT '申请内容',
  `apply_type` TINYINT DEFAULT 1 COMMENT '申请方式：1网上 2现场 3信函 4传真',
  `reply_content` TEXT COMMENT '答复内容',
  `reply_time` DATETIME COMMENT '答复时间',
  `reply_by` BIGINT COMMENT '答复人ID',
  `attachment_url` VARCHAR(500) COMMENT '附件URL',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0待处理 1已受理 2已答复 3不予公开',
  `reject_reason` VARCHAR(500) COMMENT '不予公开原因',

  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_apply_no` (`apply_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='依申请公开表';

-- -----------------------------------------------------------
-- 5. 内容审核表 t_open_content
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_open_content` (
  `id` BIGINT NOT NULL COMMENT '主键（雪花ID）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT COMMENT '创建人ID',
  `update_by` BIGINT COMMENT '更新人ID',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',

  `content_type` TINYINT NOT NULL COMMENT '内容类型：1通知公告 2政策法规 3依申请公开',
  `content_id` BIGINT NOT NULL COMMENT '内容ID',
  `audit_status` TINYINT DEFAULT 0 COMMENT '审核状态：0待审核 1审核通过 2审核驳回',
  `audit_opinion` VARCHAR(500) COMMENT '审核意见',
  `audit_time` DATETIME COMMENT '审核时间',
  `audit_by` BIGINT COMMENT '审核人ID',

  PRIMARY KEY (`id`),
  KEY `idx_content_type_id` (`content_type`, `content_id`),
  KEY `idx_audit_status` (`audit_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='内容审核表';

-- -----------------------------------------------------------
-- 6. 公开反馈表 t_open_feedback
-- -----------------------------------------------------------
CREATE TABLE IF NOT EXISTS `t_open_feedback` (
  `id` BIGINT NOT NULL COMMENT '主键（雪花ID）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT COMMENT '创建人ID',
  `update_by` BIGINT COMMENT '更新人ID',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',

  `content_type` TINYINT NOT NULL COMMENT '内容类型：1通知公告 2政策法规',
  `content_id` BIGINT NOT NULL COMMENT '内容ID',
  `user_id` BIGINT COMMENT '反馈用户ID',
  `feedback_type` TINYINT DEFAULT 1 COMMENT '反馈类型：1咨询 2建议 3投诉',
  `feedback_content` TEXT COMMENT '反馈内容',
  `reply_content` TEXT COMMENT '回复内容',
  `reply_time` DATETIME COMMENT '回复时间',
  `reply_by` BIGINT COMMENT '回复人ID',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0待处理 1已回复 2已关闭',

  PRIMARY KEY (`id`),
  KEY `idx_content_type_id` (`content_type`, `content_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公开反馈表';
