SET NAMES utf8mb4;
-- ============================================================
-- gov_reception 数据库 - 表结构补全
-- 将 04-gov_reception.sql 的基础表结构补全为 Entity 所需的完整字段
-- 创建人：组长（组员A）  创建时间：2026-06-28
-- 注意：仅在 docker-compose down -v 后重建时执行
-- ============================================================

USE `gov_reception`;

-- 1. t_reception_record：窗口ID、受理人ID、流程实例ID、驳回原因
ALTER TABLE `t_reception_record`
  ADD COLUMN `window_id` BIGINT COMMENT '受理窗口ID' AFTER `dept_id`;
ALTER TABLE `t_reception_record`
  ADD COLUMN `operator_id` BIGINT COMMENT '受理人ID' AFTER `window_id`;
ALTER TABLE `t_reception_record`
  ADD COLUMN `process_instance_id` VARCHAR(100) COMMENT '流程实例ID' AFTER `finish_time`;
ALTER TABLE `t_reception_record`
  ADD COLUMN `reject_reason` VARCHAR(500) COMMENT '驳回原因' AFTER `remark`;

-- 2. t_reception_material：文件大小、文件类型、是否必须
ALTER TABLE `t_reception_material`
  ADD COLUMN `file_size` BIGINT COMMENT '文件大小（字节）' AFTER `file_url`;
ALTER TABLE `t_reception_material`
  ADD COLUMN `file_type` VARCHAR(20) COMMENT '文件类型（jpg/png/pdf）' AFTER `file_size`;
ALTER TABLE `t_reception_material`
  ADD COLUMN `is_required` TINYINT DEFAULT 1 COMMENT '是否必须：0否 1是' AFTER `file_type`;

-- 3. t_reception_form_data：表单版本号
ALTER TABLE `t_reception_form_data`
  ADD COLUMN `form_version` INT DEFAULT 1 COMMENT '表单版本号' AFTER `form_data`;

-- 4. t_reception_output：出件编号、文件路径、领取人ID、领取时间
ALTER TABLE `t_reception_output`
  ADD COLUMN `output_no` VARCHAR(100) COMMENT '出件编号' AFTER `license_id`;
ALTER TABLE `t_reception_output`
  ADD COLUMN `file_url` VARCHAR(500) COMMENT '文件路径' AFTER `output_name`;
ALTER TABLE `t_reception_output`
  ADD COLUMN `receiver_id` BIGINT COMMENT '领取人ID' AFTER `file_url`;
ALTER TABLE `t_reception_output`
  ADD COLUMN `receive_time` DATETIME COMMENT '领取时间' AFTER `receiver_id`;

-- 5. t_reception_queue：优先级
ALTER TABLE `t_reception_queue`
  ADD COLUMN `priority` INT DEFAULT 0 COMMENT '优先级（数字越大优先级越高）' AFTER `status`;
