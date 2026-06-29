SET NAMES utf8mb4;
USE gov_datashare;

-- 数据源表
CREATE TABLE IF NOT EXISTS t_datasource (
    id              BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    source_code     VARCHAR(64)  NOT NULL COMMENT '数据源编码',
    source_name     VARCHAR(128) NOT NULL COMMENT '数据源名称',
    source_type     VARCHAR(32)  NOT NULL COMMENT '数据源类型',
    db_host         VARCHAR(128) DEFAULT NULL COMMENT '数据库主机',
    db_port         INT          DEFAULT NULL COMMENT '数据库端口',
    db_name         VARCHAR(64)  DEFAULT NULL COMMENT '数据库名称',
    db_username     VARCHAR(64)  DEFAULT NULL COMMENT '数据库用户名[脱敏][展示掩码]',
    db_password     VARCHAR(128) DEFAULT NULL COMMENT '数据库密码[脱敏][加密存储]',
    api_url         VARCHAR(512) DEFAULT NULL COMMENT '接口URL',
    api_method      VARCHAR(16)  DEFAULT NULL COMMENT '请求方法',
    api_key         VARCHAR(128) DEFAULT NULL COMMENT '接口密钥[脱敏][加密存储]',
    dept_id         BIGINT       NOT NULL COMMENT '所属部门ID',
    dept_name       VARCHAR(128) DEFAULT NULL COMMENT '所属部门名称',
    status          VARCHAR(32)  DEFAULT '1' COMMENT '状态',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_source_code (source_code),
    KEY idx_dept_id (dept_id),
    KEY idx_source_type (source_type),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据源表';

-- 共享接口表
CREATE TABLE IF NOT EXISTS t_share_api (
    id              BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    api_code        VARCHAR(64)  NOT NULL COMMENT '接口编码',
    api_name        VARCHAR(128) NOT NULL COMMENT '接口名称',
    source_id       BIGINT       NOT NULL COMMENT '数据源ID',
    api_url         VARCHAR(512) NOT NULL COMMENT '接口URL',
    api_method      VARCHAR(16)  DEFAULT 'GET' COMMENT '请求方法',
    request_params  TEXT         DEFAULT NULL COMMENT '请求参数JSON',
    response_params TEXT         DEFAULT NULL COMMENT '响应参数JSON',
    api_desc        VARCHAR(512) DEFAULT NULL COMMENT '接口描述',
    auth_type       VARCHAR(32)  DEFAULT NULL COMMENT '认证方式',
    timeout         INT          DEFAULT 5000 COMMENT '超时时间（毫秒）',
    rate_limit      INT          DEFAULT NULL COMMENT '限流阀值',
    dept_id         BIGINT       NOT NULL COMMENT '所属部门ID',
    dept_name       VARCHAR(128) DEFAULT NULL COMMENT '所属部门名称',
    status          VARCHAR(32)  DEFAULT '1' COMMENT '状态',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_api_code (api_code),
    KEY idx_source_id (source_id),
    KEY idx_dept_id (dept_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='共享接口表';

-- 共享目录表
CREATE TABLE IF NOT EXISTS t_share_catalog (
    id              BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    catalog_code    VARCHAR(64)  NOT NULL COMMENT '目录编码',
    catalog_name    VARCHAR(128) NOT NULL COMMENT '目录名称',
    parent_id       BIGINT       DEFAULT 0 COMMENT '父级ID',
    catalog_level   INT          DEFAULT 1 COMMENT '目录层级',
    data_type       VARCHAR(32)  DEFAULT NULL COMMENT '数据类型',
    data_count      INT          DEFAULT 0 COMMENT '数据量',
    dept_id         BIGINT       NOT NULL COMMENT '所属部门ID',
    status          VARCHAR(32)  DEFAULT '1' COMMENT '状态',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_catalog_code (catalog_code),
    KEY idx_parent_id (parent_id),
    KEY idx_dept_id (dept_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='共享目录表';

-- 接口权限表
CREATE TABLE IF NOT EXISTS t_share_permission (
    id              BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    api_id          BIGINT       NOT NULL COMMENT '接口ID',
    dept_id         BIGINT       NOT NULL COMMENT '申请部门ID',
    dept_name       VARCHAR(128) DEFAULT NULL COMMENT '申请部门名称',
    permission_type VARCHAR(32)  DEFAULT NULL COMMENT '权限类型',
    expire_time     DATETIME     DEFAULT NULL COMMENT '权限过期时间',
    call_limit      INT          DEFAULT NULL COMMENT '调用次数限制',
    status          VARCHAR(32)  DEFAULT '1' COMMENT '状态',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    KEY idx_api_id (api_id),
    KEY idx_dept_id (dept_id),
    KEY idx_status (status),
    KEY idx_expire_time (expire_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='接口权限表';

-- 订阅记录表
CREATE TABLE IF NOT EXISTS t_share_subscribe (
    id                BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    api_id            BIGINT       NOT NULL COMMENT '接口ID',
    api_name          VARCHAR(128) DEFAULT NULL COMMENT '接口名称',
    subscribe_dept_id BIGINT       NOT NULL COMMENT '订阅部门ID',
    subscribe_dept_name VARCHAR(128) DEFAULT NULL COMMENT '订阅部门名称',
    subscribe_user_id BIGINT       NOT NULL COMMENT '订阅人ID',
    subscribe_user_name VARCHAR(64) DEFAULT NULL COMMENT '订阅人姓名',
    subscribe_time    DATETIME     NOT NULL COMMENT '订阅时间',
    subscribe_type    VARCHAR(32)  DEFAULT NULL COMMENT '订阅类型',
    sync_interval     INT          DEFAULT NULL COMMENT '同步间隔（分钟）',
    last_sync_time    DATETIME     DEFAULT NULL COMMENT '最后同步时间',
    status            VARCHAR(32)  DEFAULT '1' COMMENT '状态',
    create_time       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time       DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by         BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by         BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted           TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    KEY idx_api_id (api_id),
    KEY idx_subscribe_dept_id (subscribe_dept_id),
    KEY idx_subscribe_user_id (subscribe_user_id),
    KEY idx_status (status),
    KEY idx_last_sync_time (last_sync_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订阅记录表';

-- 交换日志表
CREATE TABLE IF NOT EXISTS t_share_log (
    id                BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    api_id            BIGINT       DEFAULT NULL COMMENT '接口ID',
    api_code          VARCHAR(64)  DEFAULT NULL COMMENT '接口编码',
    caller_dept_id    BIGINT       DEFAULT NULL COMMENT '调用部门ID',
    caller_dept_name  VARCHAR(128) DEFAULT NULL COMMENT '调用部门名称',
    caller_user_id    BIGINT       DEFAULT NULL COMMENT '调用人ID',
    call_time         DATETIME     NOT NULL COMMENT '调用时间',
    call_params       TEXT         DEFAULT NULL COMMENT '调用参数',
    call_result       VARCHAR(32)  DEFAULT NULL COMMENT '调用结果',
    call_msg          VARCHAR(512) DEFAULT NULL COMMENT '调用信息',
    response_time     BIGINT       DEFAULT NULL COMMENT '响应时间（毫秒）',
    data_count        INT          DEFAULT 0 COMMENT '返回数据量',
    call_ip           VARCHAR(64)  DEFAULT NULL COMMENT '调用IP',
    create_time       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time       DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by         BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by         BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted           TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    KEY idx_api_id (api_id),
    KEY idx_api_code (api_code),
    KEY idx_caller_dept_id (caller_dept_id),
    KEY idx_call_time (call_time),
    KEY idx_call_result (call_result)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交换日志表';
