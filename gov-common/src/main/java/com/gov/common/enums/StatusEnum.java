package com.gov.common.enums;

import lombok.Getter;

/**
 * 状态枚举
 */
@Getter
public enum StatusEnum {

    NORMAL(0, "正常"),
    DISABLED(1, "禁用"),
    DELETED(2, "已删除");

    private final int code;
    private final String name;

    StatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}