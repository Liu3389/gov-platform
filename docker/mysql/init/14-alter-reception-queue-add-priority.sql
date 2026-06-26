-- ============================================================
-- 为t_reception_queue表添加priority字段
-- 创建人：成员B
-- 创建时间：2024
-- ============================================================

USE `gov_reception`;

-- 添加priority字段
ALTER TABLE `t_reception_queue` ADD COLUMN `priority` INT DEFAULT 0 COMMENT '优先级（数字越大优先级越高）' AFTER `status`;
