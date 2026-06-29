SET NAMES utf8mb4;
USE gov_complaint;

-- 投诉分类表
CREATE TABLE IF NOT EXISTS t_complaint_category (
    id              BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    category_name   VARCHAR(64)  NOT NULL COMMENT '分类名称',
    category_code   VARCHAR(64)  NOT NULL COMMENT '分类编码',
    parent_id       BIGINT       DEFAULT 0 COMMENT '父级ID',
    keywords        VARCHAR(256) DEFAULT NULL COMMENT '关键词',
    default_dept_id BIGINT       DEFAULT NULL COMMENT '默认处理部门ID',
    sort            INT          DEFAULT 0 COMMENT '排序',
    status          VARCHAR(32)  DEFAULT '1' COMMENT '状态',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_category_code (category_code),
    KEY idx_parent_id (parent_id),
    KEY idx_dept_id (default_dept_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='投诉分类表';

-- 投诉工单表
CREATE TABLE IF NOT EXISTS t_complaint_work (
    id                BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    work_no           VARCHAR(64)  NOT NULL COMMENT '工单编号',
    user_id           BIGINT       NOT NULL COMMENT '投诉人ID',
    user_name         VARCHAR(64)  DEFAULT NULL COMMENT '投诉人姓名',
    user_phone        VARCHAR(32)  DEFAULT NULL COMMENT '投诉人电话[脱敏][展示掩码]',
    user_id_card      VARCHAR(32)  DEFAULT NULL COMMENT '投诉人身份证号[脱敏][SM4加密]',
    category_id       BIGINT       NOT NULL COMMENT '分类ID',
    category_name     VARCHAR(128) DEFAULT NULL COMMENT '分类名称',
    complaint_type    VARCHAR(32)  DEFAULT NULL COMMENT '投诉类型',
    title             VARCHAR(256) NOT NULL COMMENT '投诉标题',
    content           TEXT         DEFAULT NULL COMMENT '投诉内容',
    images            VARCHAR(1024) DEFAULT NULL COMMENT '图片附件',
    dept_id           BIGINT       DEFAULT NULL COMMENT '处理部门ID',
    dept_name         VARCHAR(128) DEFAULT NULL COMMENT '处理部门名称',
    handler_id        BIGINT       DEFAULT NULL COMMENT '处理人ID',
    handler_name      VARCHAR(64)  DEFAULT NULL COMMENT '处理人姓名',
    status            VARCHAR(32)  DEFAULT '0' COMMENT '工单状态',
    submit_time       DATETIME     NOT NULL COMMENT '提交时间',
    assign_time       DATETIME     DEFAULT NULL COMMENT '分配时间',
    handle_time       DATETIME     DEFAULT NULL COMMENT '处理时间',
    finish_time       DATETIME     DEFAULT NULL COMMENT '办结时间',
    satisfaction      INT          DEFAULT NULL COMMENT '满意度评分',
    satisfaction_time DATETIME     DEFAULT NULL COMMENT '满意度评价时间',
    remark            VARCHAR(512) DEFAULT NULL COMMENT '备注',
    create_time       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time       DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by         BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by         BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted           TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_work_no (work_no),
    KEY idx_user_id (user_id),
    KEY idx_category_id (category_id),
    KEY idx_dept_id (dept_id),
    KEY idx_handler_id (handler_id),
    KEY idx_status (status),
    KEY idx_submit_time (submit_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='投诉工单表';

-- 处理反馈表
CREATE TABLE IF NOT EXISTS t_complaint_handle (
    id              BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    work_id         BIGINT       NOT NULL COMMENT '工单ID',
    handler_id      BIGINT       NOT NULL COMMENT '处理人ID',
    handler_name    VARCHAR(64)  DEFAULT NULL COMMENT '处理人姓名',
    handle_type     VARCHAR(32)  DEFAULT NULL COMMENT '处理类型',
    handle_content  TEXT         DEFAULT NULL COMMENT '处理内容',
    handle_time     DATETIME     NOT NULL COMMENT '处理时间',
    next_dept_id    BIGINT       DEFAULT NULL COMMENT '下一部门ID',
    next_dept_name  VARCHAR(128) DEFAULT NULL COMMENT '下一部门名称',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    KEY idx_work_id (work_id),
    KEY idx_handler_id (handler_id),
    KEY idx_handle_time (handle_time),
    KEY idx_next_dept_id (next_dept_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='处理反馈表';

-- 督办记录表
CREATE TABLE IF NOT EXISTS t_complaint_supervise (
    id                BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    work_id           BIGINT       NOT NULL COMMENT '工单ID',
    supervise_level   VARCHAR(32)  DEFAULT NULL COMMENT '督办级别',
    supervise_time    DATETIME     NOT NULL COMMENT '督办时间',
    supervise_by      BIGINT       NOT NULL COMMENT '督办人ID',
    supervise_content TEXT         DEFAULT NULL COMMENT '督办内容',
    deadline          DATETIME     DEFAULT NULL COMMENT '办理期限',
    status            VARCHAR(32)  DEFAULT '0' COMMENT '状态',
    create_time       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time       DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by         BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by         BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted           TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    KEY idx_work_id (work_id),
    KEY idx_supervise_by (supervise_by),
    KEY idx_status (status),
    KEY idx_supervise_time (supervise_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='督办记录表';

-- 建议征集表
CREATE TABLE IF NOT EXISTS t_suggestion (
    id                BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    suggestion_no     VARCHAR(64)  NOT NULL COMMENT '建议编号',
    user_id           BIGINT       NOT NULL COMMENT '建议人ID',
    user_name         VARCHAR(64)  DEFAULT NULL COMMENT '建议人姓名',
    title             VARCHAR(256) NOT NULL COMMENT '建议标题',
    content           TEXT         DEFAULT NULL COMMENT '建议内容',
    suggestion_type   VARCHAR(32)  DEFAULT NULL COMMENT '建议类型',
    status            VARCHAR(32)  DEFAULT '0' COMMENT '状态',
    reply_content     TEXT         DEFAULT NULL COMMENT '回复内容',
    reply_time        DATETIME     DEFAULT NULL COMMENT '回复时间',
    reply_by          BIGINT       DEFAULT NULL COMMENT '回复人ID',
    create_time       DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time       DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by         BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by         BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted           TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_suggestion_no (suggestion_no),
    KEY idx_user_id (user_id),
    KEY idx_status (status),
    KEY idx_suggestion_type (suggestion_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='建议征集表';

-- 热点分析表
CREATE TABLE IF NOT EXISTS t_complaint_hotspot (
    id              BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    keyword         VARCHAR(128) NOT NULL COMMENT '热点关键词',
    keyword_count   INT          DEFAULT 0 COMMENT '关键词出现次数',
    stat_date       DATE         NOT NULL COMMENT '统计日期',
    category_id     BIGINT       DEFAULT NULL COMMENT '分类ID',
    trend           VARCHAR(32)  DEFAULT NULL COMMENT '趋势',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    KEY idx_keyword (keyword),
    KEY idx_stat_date (stat_date),
    KEY idx_category_id (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='热点分析表';
