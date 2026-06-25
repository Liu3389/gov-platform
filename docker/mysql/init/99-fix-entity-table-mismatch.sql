-- =====================================================
-- 智慧政务一体化便民服务平台 - 表结构修复
-- 修复实体类与表结构不匹配问题（添加 BaseEntity 缺失字段）
-- 修复时间：2026-06-24
-- =====================================================

-- =====================================================
-- gov_user 库修复
-- =====================================================

USE gov_user;

-- 修复 t_user_role 表：添加 update_time, create_by, update_by
ALTER TABLE `t_user_role`
ADD COLUMN `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' AFTER `create_time`,
ADD COLUMN `create_by` BIGINT COMMENT '创建人ID' AFTER `update_time`,
ADD COLUMN `update_by` BIGINT COMMENT '更新人ID' AFTER `create_by`;

-- 修复 t_role_menu 表：添加 update_time, create_by, update_by
ALTER TABLE `t_role_menu`
ADD COLUMN `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' AFTER `create_time`,
ADD COLUMN `create_by` BIGINT COMMENT '创建人ID' AFTER `update_time`,
ADD COLUMN `update_by` BIGINT COMMENT '更新人ID' AFTER `create_by`;

-- 修复 t_login_log 表：添加 update_time, create_by, update_by
ALTER TABLE `t_login_log`
ADD COLUMN `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' AFTER `create_time`,
ADD COLUMN `create_by` BIGINT COMMENT '创建人ID' AFTER `update_time`,
ADD COLUMN `update_by` BIGINT COMMENT '更新人ID' AFTER `create_by`;

-- 修复 t_user_realname 表：添加 create_by, update_by（已有 update_time）
ALTER TABLE `t_user_realname`
ADD COLUMN `create_by` BIGINT COMMENT '创建人ID' AFTER `update_time`,
ADD COLUMN `update_by` BIGINT COMMENT '更新人ID' AFTER `create_by`;

-- =====================================================
-- gov_monitor 库修复
-- =====================================================

USE gov_monitor;

-- 修复 t_operate_log 表：添加 update_time, create_by, update_by
ALTER TABLE `t_operate_log`
ADD COLUMN `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' AFTER `create_time`,
ADD COLUMN `create_by` BIGINT COMMENT '创建人ID' AFTER `update_time`,
ADD COLUMN `update_by` BIGINT COMMENT '更新人ID' AFTER `create_by`;
