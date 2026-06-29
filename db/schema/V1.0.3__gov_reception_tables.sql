SET NAMES utf8mb4;
USE gov_reception;

-- 办件主表
CREATE TABLE IF NOT EXISTS t_reception_record (
    id              BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    apply_no        VARCHAR(64)  NOT NULL COMMENT '办件编号',
    item_id         BIGINT       NOT NULL COMMENT '事项ID',
    user_id         BIGINT       NOT NULL COMMENT '申请人ID',
    dept_id         BIGINT       NOT NULL COMMENT '受理部门ID',
    apply_type      VARCHAR(32)  DEFAULT NULL COMMENT '办件类型',
    status          VARCHAR(32)  NOT NULL DEFAULT '0' COMMENT '办件状态',
    urgency_type    VARCHAR(32)  DEFAULT NULL COMMENT '紧急程度',
    accept_time     DATETIME     DEFAULT NULL COMMENT '受理时间',
    finish_time     DATETIME     DEFAULT NULL COMMENT '办结时间',
    remark          VARCHAR(512) DEFAULT NULL COMMENT '备注',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_apply_no (apply_no),
    KEY idx_user_id (user_id),
    KEY idx_item_id (item_id),
    KEY idx_dept_id (dept_id),
    KEY idx_status (status),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='办件主表';

-- 申报材料表
CREATE TABLE IF NOT EXISTS t_reception_material (
    id              BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    record_id       BIGINT       NOT NULL COMMENT '办件ID',
    material_id     BIGINT       DEFAULT NULL COMMENT '材料定义ID',
    material_name   VARCHAR(128) NOT NULL COMMENT '材料名称',
    material_type   VARCHAR(32)  DEFAULT NULL COMMENT '材料类型',
    file_url        VARCHAR(512) DEFAULT NULL COMMENT '文件地址',
    file_name       VARCHAR(256) DEFAULT NULL COMMENT '文件名',
    verify_status   VARCHAR(32)  DEFAULT NULL COMMENT '核验状态',
    verify_time     DATETIME     DEFAULT NULL COMMENT '核验时间',
    verify_by       BIGINT       DEFAULT NULL COMMENT '核验人ID',
    verify_remark   VARCHAR(512) DEFAULT NULL COMMENT '核验备注',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    KEY idx_record_id (record_id),
    KEY idx_material_id (material_id),
    KEY idx_verify_status (verify_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='申报材料表';

-- 表单数据表
CREATE TABLE IF NOT EXISTS t_reception_form_data (
    id              BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    record_id       BIGINT NOT NULL COMMENT '办件ID',
    form_id         BIGINT NOT NULL COMMENT '表单ID',
    form_data       TEXT         DEFAULT NULL COMMENT '表单数据JSON',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    KEY idx_record_id (record_id),
    KEY idx_form_id (form_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='表单数据表';

-- 办件日志表
CREATE TABLE IF NOT EXISTS t_reception_log (
    id              BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    record_id       BIGINT       NOT NULL COMMENT '办件ID',
    action_type     VARCHAR(32)  NOT NULL COMMENT '操作类型',
    action_time     DATETIME     DEFAULT NULL COMMENT '操作时间',
    operator_id     BIGINT       DEFAULT NULL COMMENT '操作人ID',
    operator_name   VARCHAR(64)  DEFAULT NULL COMMENT '操作人姓名',
    remark          VARCHAR(512) DEFAULT NULL COMMENT '备注',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    KEY idx_record_id (record_id),
    KEY idx_action_type (action_type),
    KEY idx_operator_id (operator_id),
    KEY idx_action_time (action_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='办件日志表';

-- 出件记录表
CREATE TABLE IF NOT EXISTS t_reception_output (
    id              BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    record_id       BIGINT       NOT NULL COMMENT '办件ID',
    output_type     VARCHAR(32)  DEFAULT NULL COMMENT '出件类型',
    output_name     VARCHAR(128) DEFAULT NULL COMMENT '出件名称',
    license_id      BIGINT       DEFAULT NULL COMMENT '证照ID',
    license_no      VARCHAR(64)  DEFAULT NULL COMMENT '证照编号',
    output_time     DATETIME     DEFAULT NULL COMMENT '出件时间',
    output_by       BIGINT       DEFAULT NULL COMMENT '出件人ID',
    pickup_type     VARCHAR(32)  DEFAULT NULL COMMENT '领取方式',
    pickup_time     DATETIME     DEFAULT NULL COMMENT '领取时间',
    pickup_by       BIGINT       DEFAULT NULL COMMENT '领取人ID',
    pickup_remark   VARCHAR(512) DEFAULT NULL COMMENT '领取备注',
    status          VARCHAR(32)  DEFAULT NULL COMMENT '出件状态',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    KEY idx_record_id (record_id),
    KEY idx_license_id (license_id),
    KEY idx_status (status),
    KEY idx_output_time (output_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='出件记录表';

-- 窗口信息表
CREATE TABLE IF NOT EXISTS t_reception_window (
    id              BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    window_no       VARCHAR(32)  NOT NULL COMMENT '窗口编号',
    window_name     VARCHAR(64)  NOT NULL COMMENT '窗口名称',
    dept_id         BIGINT       NOT NULL COMMENT '所属部门ID',
    staff_id        BIGINT       DEFAULT NULL COMMENT '工作人员ID',
    staff_name      VARCHAR(64)  DEFAULT NULL COMMENT '工作人员姓名',
    window_type     VARCHAR(32)  DEFAULT NULL COMMENT '窗口类型',
    status          VARCHAR(32)  DEFAULT '1' COMMENT '状态',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_window_no (window_no),
    KEY idx_dept_id (dept_id),
    KEY idx_staff_id (staff_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='窗口信息表';

-- 排队叫号表
CREATE TABLE IF NOT EXISTS t_reception_queue (
    id              BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    queue_no        VARCHAR(32)  NOT NULL COMMENT '排队编号',
    user_id         BIGINT       NOT NULL COMMENT '申请人ID',
    user_name       VARCHAR(64)  DEFAULT NULL COMMENT '申请人姓名',
    window_id       BIGINT       DEFAULT NULL COMMENT '窗口ID',
    window_no       VARCHAR(32)  DEFAULT NULL COMMENT '窗口编号',
    item_id         BIGINT       DEFAULT NULL COMMENT '事项ID',
    item_name       VARCHAR(128) DEFAULT NULL COMMENT '事项名称',
    queue_time      DATETIME     NOT NULL COMMENT '排队时间',
    call_time       DATETIME     DEFAULT NULL COMMENT '叫号时间',
    call_by         BIGINT       DEFAULT NULL COMMENT '叫号人ID',
    finish_time     DATETIME     DEFAULT NULL COMMENT '完成时间',
    status          VARCHAR(32)  DEFAULT '0' COMMENT '排队状态',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_queue_no (queue_no),
    KEY idx_user_id (user_id),
    KEY idx_window_id (window_id),
    KEY idx_item_id (item_id),
    KEY idx_status (status),
    KEY idx_queue_time (queue_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='排队叫号表';
