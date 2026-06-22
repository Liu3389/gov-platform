package com.gov.common.exception;

import lombok.Getter;

/**
 * 业务异常
 * 所有业务逻辑异常必须抛出此异常，由全局异常处理器统一处理
 */
@Getter
public class BusinessException extends RuntimeException {

    /** 错误码 */
    private final int code;

    /** 错误消息 */
    private final String message;

    public BusinessException(String message) {
        super(message);
        this.code = 500;
        this.message = message;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.code = 500;
        this.message = message;
    }

    // ==================== 常用异常 ====================

    /**
     * 参数错误
     */
    public static BusinessException paramError(String message) {
        return new BusinessException(400, message);
    }

    /**
     * 数据不存在
     */
    public static BusinessException notFound(String message) {
        return new BusinessException(404, message);
    }

    /**
     * 数据已存在
     */
    public static BusinessException alreadyExists(String message) {
        return new BusinessException(409, message);
    }

    /**
     * 操作失败
     */
    public static BusinessException fail(String message) {
        return new BusinessException(500, message);
    }
}