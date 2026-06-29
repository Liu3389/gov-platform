SET NAMES utf8mb4;
-- =====================================================
-- 智慧政务一体化便民服务平台 - 数据库初始化脚本
-- 创建所有微服务数据库（共11个库：nacos + 10个业务库）
-- =====================================================

-- 1. Nacos 配置中心数据库（Nacos 容器启动时会自动创建表，这里只建库）
CREATE DATABASE IF NOT EXISTS nacos DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 2. 用户认证库
CREATE DATABASE IF NOT EXISTS gov_user DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 3. 事项管理库
CREATE DATABASE IF NOT EXISTS gov_item DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 4. 统一受理库
CREATE DATABASE IF NOT EXISTS gov_reception DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 5. 审批流转库
CREATE DATABASE IF NOT EXISTS gov_activiti DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 6. 电子证照库
CREATE DATABASE IF NOT EXISTS gov_license DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 7. 投诉建议库
CREATE DATABASE IF NOT EXISTS gov_complaint DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 8. 政务公开库
CREATE DATABASE IF NOT EXISTS gov_open DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 9. 数据共享库
CREATE DATABASE IF NOT EXISTS gov_datashare DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 10. 消息通知库
CREATE DATABASE IF NOT EXISTS gov_message DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 11. 监察审计库
CREATE DATABASE IF NOT EXISTS gov_monitor DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- =====================================================
-- 授权各数据库访问权限（可选，生产环境建议创建专用用户）
-- =====================================================
-- GRANT ALL PRIVILEGES ON gov_*.* TO 'gov_user'@'%' IDENTIFIED BY 'gov_password';
-- FLUSH PRIVILEGES;