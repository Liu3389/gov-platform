package com.gov.common.enums;

import lombok.Getter;

/**
 * 审批状态枚举
 */
@Getter
public enum ApprovalStatusEnum {

    PENDING(0, "待受理"),
    ACCEPTED(1, "受理中"),
    APPROVING(2, "审批中"),
    FINISHED(3, "办结"),
    REJECTED(4, "驳回");

    private final int code;
    private final String name;

    ApprovalStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ApprovalStatusEnum of(int code) {
        for (ApprovalStatusEnum status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null;
    }
}