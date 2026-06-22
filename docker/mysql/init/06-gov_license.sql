USE gov_license;

-- 证照目录表
CREATE TABLE IF NOT EXISTS t_license_catalog (
    id              BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    catalog_code    VARCHAR(64)  NOT NULL COMMENT '证照目录编码',
    catalog_name    VARCHAR(128) NOT NULL COMMENT '证照目录名称',
    catalog_type    VARCHAR(32)  DEFAULT NULL COMMENT '证照类型',
    dept_id         BIGINT       NOT NULL COMMENT '颁发部门ID',
    dept_name       VARCHAR(128) DEFAULT NULL COMMENT '颁发部门名称',
    template_url    VARCHAR(512) DEFAULT NULL COMMENT '模板地址',
    template_type   VARCHAR(32)  DEFAULT NULL COMMENT '模板类型',
    valid_years     INT          DEFAULT NULL COMMENT '有效期（年）',
    sign_flag       TINYINT      DEFAULT 0 COMMENT '签章标记：0不需要 1需要',
    qr_flag         TINYINT      DEFAULT 0 COMMENT '二维码标记：0不需要 1需要',
    status          VARCHAR(32)  DEFAULT '1' COMMENT '状态',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_catalog_code (catalog_code),
    KEY idx_dept_id (dept_id),
    KEY idx_catalog_type (catalog_type),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='证照目录表';

-- 证照模板表
CREATE TABLE IF NOT EXISTS t_license_template (
    id              BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    catalog_id      BIGINT       NOT NULL COMMENT '证照目录ID',
    template_name   VARCHAR(128) NOT NULL COMMENT '模板名称',
    template_url    VARCHAR(512) NOT NULL COMMENT '模板地址',
    template_type   VARCHAR(32)  DEFAULT NULL COMMENT '模板类型',
    field_config    TEXT         DEFAULT NULL COMMENT '字段配置JSON',
    sign_config     TEXT         DEFAULT NULL COMMENT '签章配置JSON',
    version         INT          DEFAULT 1 COMMENT '版本号',
    status          VARCHAR(32)  DEFAULT '1' COMMENT '状态',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    KEY idx_catalog_id (catalog_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='证照模板表';

-- 证照数据表
CREATE TABLE IF NOT EXISTS t_license_data (
    id              BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    license_no      VARCHAR(64)  NOT NULL COMMENT '证照编号',
    catalog_id      BIGINT       NOT NULL COMMENT '证照目录ID',
    catalog_name    VARCHAR(128) DEFAULT NULL COMMENT '证照目录名称',
    user_id         BIGINT       NOT NULL COMMENT '持证人ID',
    user_name       VARCHAR(64)  NOT NULL COMMENT '持证人姓名',
    user_id_card    VARCHAR(32)  DEFAULT NULL COMMENT '持证人身份证号[脱敏][SM4加密]',
    apply_no        VARCHAR(64)  DEFAULT NULL COMMENT '办件编号',
    license_content TEXT         DEFAULT NULL COMMENT '证照内容JSON',
    file_url        VARCHAR(512) DEFAULT NULL COMMENT '证照文件地址',
    qr_code         VARCHAR(512) DEFAULT NULL COMMENT '二维码图片地址',
    qr_content      VARCHAR(512) DEFAULT NULL COMMENT '二维码内容',
    sign_time       DATETIME     DEFAULT NULL COMMENT '签章时间',
    sign_by         BIGINT       DEFAULT NULL COMMENT '签章人ID',
    expire_time     DATETIME     DEFAULT NULL COMMENT '过期时间',
    status          VARCHAR(32)  DEFAULT '1' COMMENT '证照状态',
    cancel_time     DATETIME     DEFAULT NULL COMMENT '注销时间',
    cancel_reason   VARCHAR(512) DEFAULT NULL COMMENT '注销原因',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_license_no (license_no),
    KEY idx_catalog_id (catalog_id),
    KEY idx_user_id (user_id),
    KEY idx_apply_no (apply_no),
    KEY idx_status (status),
    KEY idx_expire_time (expire_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='证照数据表';

-- 签章记录表
CREATE TABLE IF NOT EXISTS t_license_sign (
    id              BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    license_id      BIGINT       NOT NULL COMMENT '证照ID',
    sign_type       VARCHAR(32)  DEFAULT NULL COMMENT '签章类型',
    sign_user       BIGINT       NOT NULL COMMENT '签章人ID',
    sign_user_name  VARCHAR(64)  DEFAULT NULL COMMENT '签章人姓名',
    sign_time       DATETIME     NOT NULL COMMENT '签章时间',
    sign_result     VARCHAR(32)  DEFAULT NULL COMMENT '签章结果',
    sign_cert_sn    VARCHAR(128) DEFAULT NULL COMMENT '证书序列号',
    sign_data       TEXT         DEFAULT NULL COMMENT '签章数据',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    KEY idx_license_id (license_id),
    KEY idx_sign_user (sign_user),
    KEY idx_sign_time (sign_time),
    KEY idx_sign_result (sign_result)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='签章记录表';

-- 核验记录表
CREATE TABLE IF NOT EXISTS t_license_verify (
    id              BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    license_id      BIGINT       NOT NULL COMMENT '证照ID',
    license_no      VARCHAR(64)  NOT NULL COMMENT '证照编号',
    verify_user     BIGINT       NOT NULL COMMENT '核验人ID',
    verify_user_name VARCHAR(64) DEFAULT NULL COMMENT '核验人姓名',
    verify_time     DATETIME     NOT NULL COMMENT '核验时间',
    verify_result   VARCHAR(32)  DEFAULT NULL COMMENT '核验结果',
    verify_scene    VARCHAR(64)  DEFAULT NULL COMMENT '核验场景',
    verify_ip       VARCHAR(64)  DEFAULT NULL COMMENT '核验IP',
    verify_remark   VARCHAR(512) DEFAULT NULL COMMENT '核验备注',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    KEY idx_license_id (license_id),
    KEY idx_license_no (license_no),
    KEY idx_verify_user (verify_user),
    KEY idx_verify_time (verify_time),
    KEY idx_verify_scene (verify_scene)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='核验记录表';

-- 授权记录表
CREATE TABLE IF NOT EXISTS t_license_auth (
    id              BIGINT NOT NULL COMMENT '主键ID（雪花ID）',
    license_id      BIGINT       NOT NULL COMMENT '证照ID',
    auth_user_id    BIGINT       NOT NULL COMMENT '授权人ID',
    auth_target_id  BIGINT       NOT NULL COMMENT '被授权对象ID',
    auth_target_name VARCHAR(128) DEFAULT NULL COMMENT '被授权对象名称',
    auth_type       VARCHAR(32)  DEFAULT NULL COMMENT '授权类型',
    auth_scope      VARCHAR(256) DEFAULT NULL COMMENT '授权范围',
    auth_time       DATETIME     NOT NULL COMMENT '授权时间',
    expire_time     DATETIME     DEFAULT NULL COMMENT '过期时间',
    cancel_time     DATETIME     DEFAULT NULL COMMENT '取消时间',
    status          VARCHAR(32)  DEFAULT '1' COMMENT '授权状态',
    create_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       BIGINT       DEFAULT NULL COMMENT '创建人ID',
    update_by       BIGINT       DEFAULT NULL COMMENT '更新人ID',
    deleted         TINYINT      DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
    PRIMARY KEY (id),
    KEY idx_license_id (license_id),
    KEY idx_auth_user_id (auth_user_id),
    KEY idx_auth_target_id (auth_target_id),
    KEY idx_status (status),
    KEY idx_expire_time (expire_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='授权记录表';
