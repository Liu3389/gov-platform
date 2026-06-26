-- ============================================================
-- 为t_reception_record表添加缺失字段
-- 创建人：成员B
-- 创建时间：2024
-- ============================================================

USE `gov_reception`;

-- 添加window_id字段
ALTER TABLE `t_reception_record` ADD COLUMN `window_id` BIGINT COMMENT '受理窗口ID' AFTER `dept_id`;

-- 添加operator_id字段
ALTER TABLE `t_reception_record` ADD COLUMN `operator_id` BIGINT COMMENT '受理人ID' AFTER `window_id`;

-- 添加process_instance_id字段
ALTER TABLE `t_reception_record` ADD COLUMN `process_instance_id` VARCHAR(100) COMMENT '流程实例ID' AFTER `finish_time`;

-- 添加reject_reason字段
ALTER TABLE `t_reception_record` ADD COLUMN `reject_reason` VARCHAR(500) COMMENT '驳回原因' AFTER `remark`;
