-- ============================================================
-- 为t_reception_material表添加缺失字段
-- 创建人：成员B
-- 创建时间：2024
-- ============================================================

USE `gov_reception`;

-- 添加file_size字段
ALTER TABLE `t_reception_material` ADD COLUMN `file_size` BIGINT COMMENT '文件大小（字节）' AFTER `file_url`;

-- 添加file_type字段
ALTER TABLE `t_reception_material` ADD COLUMN `file_type` VARCHAR(20) COMMENT '文件类型（jpg/png/pdf）' AFTER `file_size`;

-- 添加is_required字段
ALTER TABLE `t_reception_material` ADD COLUMN `is_required` TINYINT DEFAULT 1 COMMENT '是否必须：0否 1是' AFTER `file_type`;
