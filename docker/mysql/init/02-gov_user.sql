-- =====================================================
-- 智慧政务一体化便民服务平台 - 建表脚本
-- 用户认证库（gov_user）— 组员A
-- =====================================================

USE gov_user;

-- 用户基本信息表
CREATE TABLE IF NOT EXISTS `t_user_info` (
  `id` BIGINT NOT NULL COMMENT '用户ID（雪花ID）',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码 [脱敏][BCrypt哈希]',
  `phone` VARCHAR(20) COMMENT '手机号 [脱敏][展示时掩码138****8000]',
  `real_name` VARCHAR(50) COMMENT '真实姓名',
  `id_card` VARCHAR(100) COMMENT '身份证号 [脱敏][SM4加密存储]',
  `email` VARCHAR(100) COMMENT '邮箱',
  `avatar` VARCHAR(200) COMMENT '头像URL',
  `gender` TINYINT DEFAULT 0 COMMENT '性别：0未知 1男 2女',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0正常 1禁用 2待实名',
  `last_login_time` DATETIME COMMENT '最后登录时间',
  `last_login_ip` VARCHAR(50) COMMENT '最后登录IP',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT COMMENT '创建人ID',
  `update_by` BIGINT COMMENT '更新人ID',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_phone` (`phone`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户基本信息';

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
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT COMMENT '创建人ID',
  `update_by` BIGINT COMMENT '更新人ID',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dept_code` (`dept_code`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门信息表';

-- 角色信息表
CREATE TABLE IF NOT EXISTS `t_role_info` (
  `id` BIGINT NOT NULL COMMENT '角色ID（雪花ID）',
  `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
  `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码（唯一）',
  `status` TINYINT DEFAULT 1 COMMENT '状态：1正常 0禁用',
  `remark` VARCHAR(200) COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT COMMENT '创建人ID',
  `update_by` BIGINT COMMENT '更新人ID',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色信息表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS `t_user_role` (
  `id` BIGINT NOT NULL COMMENT '主键（雪花ID）',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

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
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT COMMENT '创建人ID',
  `update_by` BIGINT COMMENT '更新人ID',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_menu_code` (`menu_code`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单信息表';

-- 角色菜单关联表
CREATE TABLE IF NOT EXISTS `t_role_menu` (
  `id` BIGINT NOT NULL COMMENT '主键（雪花ID）',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联表';

-- CA证书绑定表
CREATE TABLE IF NOT EXISTS `t_ca_certificate` (
  `id` BIGINT NOT NULL COMMENT '证书ID（雪花ID）',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `cert_sn` VARCHAR(100) NOT NULL COMMENT '证书序列号',
  `cert_content` TEXT COMMENT '证书内容 [脱敏][加密存储]',
  `cert_type` TINYINT DEFAULT 1 COMMENT '证书类型：1个人 2企业',
  `issuer` VARCHAR(100) COMMENT '颁发机构',
  `expire_time` DATETIME COMMENT '过期时间',
  `status` TINYINT DEFAULT 1 COMMENT '状态：1有效 2过期 3注销',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT COMMENT '创建人ID',
  `update_by` BIGINT COMMENT '更新人ID',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_cert_sn` (`cert_sn`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_expire_time` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CA证书绑定表';

-- 登录日志表
CREATE TABLE IF NOT EXISTS `t_login_log` (
  `id` BIGINT NOT NULL COMMENT '日志ID（雪花ID）',
  `user_id` BIGINT COMMENT '用户ID',
  `username` VARCHAR(50) COMMENT '登录用户名',
  `login_ip` VARCHAR(50) COMMENT '登录IP',
  `login_location` VARCHAR(100) COMMENT '登录地点',
  `login_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  `login_type` TINYINT DEFAULT 1 COMMENT '登录类型：1PC 2移动端 3窗口终端',
  `login_status` TINYINT DEFAULT 1 COMMENT '登录状态：1成功 0失败',
  `login_msg` VARCHAR(200) COMMENT '登录消息',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_login_time` (`login_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录日志表';

-- 实名认证记录表
CREATE TABLE IF NOT EXISTS `t_user_realname` (
  `id` BIGINT NOT NULL COMMENT '认证ID（雪花ID）',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `real_name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
  `id_card` VARCHAR(100) NOT NULL COMMENT '身份证号 [脱敏][SM4加密存储]',
  `cert_type` TINYINT DEFAULT 1 COMMENT '认证类型：1身份证 2护照 3其他',
  `cert_front_url` VARCHAR(200) COMMENT '证件正面照片URL',
  `cert_back_url` VARCHAR(200) COMMENT '证件背面照片URL',
  `verify_status` TINYINT DEFAULT 0 COMMENT '认证状态：0待审 1通过 2驳回',
  `verify_time` DATETIME COMMENT '认证时间',
  `verify_by` BIGINT COMMENT '审核人ID',
  `verify_remark` VARCHAR(200) COMMENT '审核备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_verify_status` (`verify_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='实名认证记录表';

-- 数据字典表
CREATE TABLE IF NOT EXISTS `t_dict_data` (
  `id` BIGINT NOT NULL COMMENT '字典ID（雪花ID）',
  `dict_type` VARCHAR(50) NOT NULL COMMENT '字典类型（如：item_type、status、gender）',
  `dict_code` VARCHAR(50) NOT NULL COMMENT '字典编码',
  `dict_name` VARCHAR(100) NOT NULL COMMENT '字典名称（显示值）',
  `dict_value` VARCHAR(50) COMMENT '字典值（存储值）',
  `parent_code` VARCHAR(50) COMMENT '父级编码（用于树形字典）',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `remark` VARCHAR(200) COMMENT '备注',
  `status` TINYINT DEFAULT 1 COMMENT '状态：1正常 0禁用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT COMMENT '创建人ID',
  `update_by` BIGINT COMMENT '更新人ID',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  KEY `idx_dict_type` (`dict_type`),
  KEY `idx_dict_code` (`dict_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据字典表';

-- 行政区划表
CREATE TABLE IF NOT EXISTS `t_region` (
  `id` BIGINT NOT NULL COMMENT '区划ID（雪花ID）',
  `region_code` VARCHAR(20) NOT NULL COMMENT '区划编码（唯一）',
  `region_name` VARCHAR(100) NOT NULL COMMENT '区划名称',
  `parent_code` VARCHAR(20) COMMENT '上级区划编码',
  `region_level` TINYINT DEFAULT 1 COMMENT '区划级别：1省 2市 3县 4乡镇',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT DEFAULT 1 COMMENT '状态：1正常 0禁用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_region_code` (`region_code`),
  KEY `idx_parent_code` (`parent_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='行政区划表';

-- 系统配置表
CREATE TABLE IF NOT EXISTS `t_config` (
  `id` BIGINT NOT NULL COMMENT '配置ID（雪花ID）',
  `config_key` VARCHAR(100) NOT NULL COMMENT '配置键（唯一）',
  `config_value` VARCHAR(500) COMMENT '配置值',
  `config_type` VARCHAR(50) COMMENT '配置类型',
  `remark` VARCHAR(200) COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT COMMENT '创建人ID',
  `update_by` BIGINT COMMENT '更新人ID',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- 文件信息表
CREATE TABLE IF NOT EXISTS `t_file_info` (
  `id` BIGINT NOT NULL COMMENT '文件ID（雪花ID）',
  `file_name` VARCHAR(200) NOT NULL COMMENT '文件名',
  `file_path` VARCHAR(500) NOT NULL COMMENT '文件路径',
  `file_type` VARCHAR(50) COMMENT '文件类型（扩展名）',
  `file_size` BIGINT COMMENT '文件大小（字节）',
  `upload_user` BIGINT COMMENT '上传人ID',
  `upload_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  `business_type` VARCHAR(50) COMMENT '业务类型',
  `business_id` BIGINT COMMENT '业务ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  PRIMARY KEY (`id`),
  KEY `idx_file_path` (`file_path`),
  KEY `idx_business` (`business_type`, `business_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件信息表';