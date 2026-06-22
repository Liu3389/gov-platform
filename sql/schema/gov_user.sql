-- =====================================================
-- 智慧政务一体化便民服务平台 - 建表脚本
-- 用户认证库（gov_user）
-- =====================================================

USE gov_user;

-- 用户基本信息表
CREATE TABLE IF NOT EXISTS `t_user_info` (
  `id` BIGINT NOT NULL COMMENT '用户ID（雪花ID）',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码（BCrypt加密）',
  `phone` VARCHAR(20) COMMENT '手机号',
  `real_name` VARCHAR(50) COMMENT '真实姓名',
  `id_card` VARCHAR(100) COMMENT '身份证号（SM4加密存储）',
  `email` VARCHAR(100) COMMENT '邮箱',
  `avatar` VARCHAR(200) COMMENT '头像URL',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0正常 1禁用 2待实名',
  `last_login_time` DATETIME COMMENT '最后登录时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` BIGINT,
  `update_by` BIGINT,
  `deleted` TINYINT DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_phone` (`phone`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户基本信息';

-- 部门信息表
CREATE TABLE IF NOT EXISTS `t_dept_info` (
  `id` BIGINT NOT NULL COMMENT '部门ID（雪花ID）',
  `dept_name` VARCHAR(50) NOT NULL COMMENT '部门名称',
  `dept_code` VARCHAR(20) NOT NULL COMMENT '部门编码（唯一）',
  `parent_id` BIGINT DEFAULT 0 COMMENT '上级部门ID',
  `dept_type` TINYINT DEFAULT 1 COMMENT '部门类型：1行政机关 2事业单位 3企业',
  `leader_id` BIGINT COMMENT '部门负责人ID',
  `phone` VARCHAR(20) COMMENT '联系电话',
  `address` VARCHAR(200) COMMENT '办公地址',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT DEFAULT 1 COMMENT '状态：1正常 0禁用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` BIGINT,
  `update_by` BIGINT,
  `deleted` TINYINT DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dept_code` (`dept_code`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门信息表';

-- 角色信息表
CREATE TABLE IF NOT EXISTS `t_role_info` (
  `id` BIGINT NOT NULL COMMENT '角色ID',
  `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
  `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码',
  `status` TINYINT DEFAULT 1 COMMENT '状态：1正常 0禁用',
  `remark` VARCHAR(200) COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` BIGINT,
  `update_by` BIGINT,
  `deleted` TINYINT DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色信息表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS `t_user_role` (
  `id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 菜单信息表
CREATE TABLE IF NOT EXISTS `t_menu` (
  `id` BIGINT NOT NULL COMMENT '菜单ID（雪花ID）',
  `menu_name` VARCHAR(50) NOT NULL COMMENT '菜单名称',
  `menu_code` VARCHAR(50) NOT NULL COMMENT '菜单编码（唯一）',
  `menu_url` VARCHAR(200) COMMENT '菜单路径',
  `menu_type` TINYINT DEFAULT 1 COMMENT '菜单类型：1目录 2菜单 3按钮',
  `parent_id` BIGINT DEFAULT 0 COMMENT '上级菜单ID',
  `icon` VARCHAR(50) COMMENT '菜单图标',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `visible` TINYINT DEFAULT 1 COMMENT '是否可见：1显示 0隐藏',
  `status` TINYINT DEFAULT 1 COMMENT '状态：1正常 0禁用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` BIGINT,
  `update_by` BIGINT,
  `deleted` TINYINT DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_menu_code` (`menu_code`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单信息表';

-- 数据字典表
CREATE TABLE IF NOT EXISTS `t_dict_data` (
  `id` BIGINT NOT NULL COMMENT '字典ID（雪花ID）',
  `dict_type` VARCHAR(50) NOT NULL COMMENT '字典类型',
  `dict_code` VARCHAR(50) NOT NULL COMMENT '字典编码',
  `dict_name` VARCHAR(100) NOT NULL COMMENT '字典名称（显示值）',
  `dict_value` VARCHAR(50) COMMENT '字典值（存储值）',
  `parent_code` VARCHAR(50) COMMENT '父级编码',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `remark` VARCHAR(200) COMMENT '备注',
  `status` TINYINT DEFAULT 1 COMMENT '状态：1正常 0禁用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_by` BIGINT,
  `update_by` BIGINT,
  `deleted` TINYINT DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_dict_type` (`dict_type`),
  KEY `idx_dict_code` (`dict_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据字典表';

-- 行政区划表
CREATE TABLE IF NOT EXISTS `t_region` (
  `id` BIGINT NOT NULL COMMENT '区划ID',
  `region_code` VARCHAR(20) NOT NULL COMMENT '区划编码（唯一）',
  `region_name` VARCHAR(100) NOT NULL COMMENT '区划名称',
  `parent_code` VARCHAR(20) COMMENT '上级区划编码',
  `region_level` TINYINT DEFAULT 1 COMMENT '区划级别：1省 2市 3县 4乡镇',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT DEFAULT 1 COMMENT '状态：1正常 0禁用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_region_code` (`region_code`),
  KEY `idx_parent_code` (`parent_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='行政区划表';