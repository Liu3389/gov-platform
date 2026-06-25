-- =====================================================
-- Nacos namespace 初始化脚本
-- 在 Nacos 控制台手动创建，或通过 API 创建
-- =====================================================
-- Nacos 控制台：http://localhost:8848/nacos
-- 默认账号密码：nacos/nacos
-- =====================================================

-- 通过 MySQL 直接插入 Nacos namespace（Nacos 2.3.2 表结构）

USE nacos;

INSERT IGNORE INTO tenant_info (tenant_id, tenant_name, tenant_desc, create_source, create_time, modify_time) VALUES
('gov-platform', 'gov-platform', '智慧政务一体化便民服务平台开发环境', 0, NOW(), NOW());

INSERT IGNORE INTO tenant_capacity (tenant_id, quota, type, max_size, max_aggr_size, max_aggr_count) VALUES
('gov-platform', 1000, 0, 0, 0, 0);