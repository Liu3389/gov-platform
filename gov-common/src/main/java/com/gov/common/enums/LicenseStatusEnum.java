package com.gov.common.enums;

import lombok.Getter;

/**
 * 证照状态枚举
 */
@Getter
public enum LicenseStatusEnum {

    VALID(1, "有效"),
    EXPIRED(2, "过期"),
    CANCELLED(3, "注销");

    private final int code;
    private final String name;

    LicenseStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static LicenseStatusEnum of(int code) {
        for (LicenseStatusEnum status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null;
    }
}