package com.gov.common.enums;

import lombok.Getter;

/**
 * 事项类型枚举
 */
@Getter
public enum ItemTypeEnum {

    ADMIN_LICENSE(1, "行政许可"),
    PUBLIC_SERVICE(2, "公共服务"),
    ADMIN_CONFIRM(3, "行政确认"),
    ADMIN_REWARD(4, "行政奖励"),
    ADMIN_PAYMENT(5, "行政给付");

    private final int code;
    private final String name;

    ItemTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ItemTypeEnum of(int code) {
        for (ItemTypeEnum type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }
}