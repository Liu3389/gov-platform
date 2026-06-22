USE gov_message;

-- 消息模板表
CREATE TABLE IF NOT EXISTS t_message_template (
    id              BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    template_code   VARCHAR(64)  NOT NULL COMMENT '模板编码',
    template_name   VARCHAR(128) NOT NULL COMMENT '模板名称',
    template_content TEXT        DEFAULT NULL COMMENT '模板内容',
    template_type   VARCHAR(32)  DEFAULT NULL COMMENT '模板类型',
    channel         VARCHAR(32)  DEFAULT NULL COMMENT '渠道',
    variables       VARCHAR(512) DEFAULT NULL COMMENT '模板变量JSON',
    status          VARCHAR(32)  DEFAULT '1' COMMENT '状态',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_template_code (template_code),
    KEY idx_channel (channel),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息模板表';

-- 渠道配置表
CREATE TABLE IF NOT EXISTS t_message_config (
    id              BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    channel         VARCHAR(32)  NOT NULL COMMENT '渠道',
    channel_name    VARCHAR(64)  DEFAULT NULL COMMENT '渠道名称',
    config_key      VARCHAR(64)  NOT NULL COMMENT '配置键',
    config_value    VARCHAR(512) DEFAULT NULL COMMENT '配置值[脱敏][加密存储]',
    config_type     VARCHAR(32)  DEFAULT NULL COMMENT '配置类型',
    provider        VARCHAR(64)  DEFAULT NULL COMMENT '服务提供方',
    status          VARCHAR(32)  DEFAULT '1' COMMENT '状态',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    KEY idx_channel_key (channel, config_key),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='渠道配置表';

-- 发送记录表
CREATE TABLE IF NOT EXISTS t_message_record (
    id              BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    template_id     BIGINT       DEFAULT NULL COMMENT '模板ID',
    template_code   VARCHAR(64)  DEFAULT NULL COMMENT '模板编码',
    receiver_id     BIGINT       NOT NULL COMMENT '接收人ID',
    receiver_name   VARCHAR(64)  DEFAULT NULL COMMENT '接收人姓名',
    receiver_phone  VARCHAR(32)  DEFAULT NULL COMMENT '接收人电话[脱敏][展示掩码]',
    receiver_email  VARCHAR(128) DEFAULT NULL COMMENT '接收人邮箱',
    content         TEXT         DEFAULT NULL COMMENT '发送内容',
    channel         VARCHAR(32)  NOT NULL COMMENT '发送渠道',
    business_type   VARCHAR(32)  DEFAULT NULL COMMENT '业务类型',
    business_id     VARCHAR(64)  DEFAULT NULL COMMENT '业务ID',
    send_time       DATETIME     NOT NULL COMMENT '发送时间',
    send_status     VARCHAR(32)  DEFAULT '0' COMMENT '发送状态',
    send_msg        VARCHAR(512) DEFAULT NULL COMMENT '发送消息',
    retry_count     INT          DEFAULT 0 COMMENT '重试次数',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    KEY idx_receiver_id (receiver_id),
    KEY idx_template_id (template_id),
    KEY idx_channel (channel),
    KEY idx_business_type_id (business_type, business_id),
    KEY idx_send_time (send_time),
    KEY idx_send_status (send_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='发送记录表';

-- 站内信表
CREATE TABLE IF NOT EXISTS t_message_inbox (
    id              BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    user_id         BIGINT       NOT NULL COMMENT '用户ID',
    title           VARCHAR(256) NOT NULL COMMENT '消息标题',
    content         TEXT         DEFAULT NULL COMMENT '消息内容',
    msg_type        VARCHAR(32)  DEFAULT NULL COMMENT '消息类型',
    business_type   VARCHAR(32)  DEFAULT NULL COMMENT '业务类型',
    business_id     VARCHAR(64)  DEFAULT NULL COMMENT '业务ID',
    is_read         TINYINT      DEFAULT 0 COMMENT '是否已读：0未读 1已读',
    read_time       DATETIME     DEFAULT NULL COMMENT '阅读时间',
    send_time       DATETIME     NOT NULL COMMENT '发送时间',
    expire_time     DATETIME     DEFAULT NULL COMMENT '过期时间',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    KEY idx_user_id (user_id),
    KEY idx_is_read (is_read),
    KEY idx_send_time (send_time),
    KEY idx_business_type_id (business_type, business_id),
    KEY idx_expire_time (expire_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='站内信表';

-- 消息队列表
CREATE TABLE IF NOT EXISTS t_message_queue (
    id              BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    record_id       BIGINT       NOT NULL COMMENT '消息记录ID',
    queue_status    VARCHAR(32)  DEFAULT '0' COMMENT '队列状态：0待发送 1发送中 2成功 3失败',
    priority        INT          DEFAULT 0 COMMENT '优先级',
    scheduled_time  DATETIME     DEFAULT NULL COMMENT '计划发送时间',
    process_time    DATETIME     DEFAULT NULL COMMENT '处理时间',
    retry_count     INT          DEFAULT 0 COMMENT '已重试次数',
    max_retry       INT          DEFAULT 3 COMMENT '最大重试次数',
    error_msg       VARCHAR(512) DEFAULT NULL COMMENT '错误信息',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    KEY idx_record_id (record_id),
    KEY idx_queue_status (queue_status),
    KEY idx_priority (priority),
    KEY idx_scheduled_time (scheduled_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息队列表';
