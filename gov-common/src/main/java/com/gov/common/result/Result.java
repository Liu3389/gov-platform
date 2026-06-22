package com.gov.common.result;

import cn.hutool.core.util.IdUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回体 Result<T>
 * 所有接口必须使用此格式返回
 *
 * @param <T> 数据类型
 */
@Data
@Schema(description = "统一返回体")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 成功状态码 */
    public static final int SUCCESS_CODE = 200;

    /** 失败状态码 */
    public static final int FAIL_CODE = 500;

    @Schema(description = "状态码：200成功，500失败")
    private int code;

    @Schema(description = "提示信息")
    private String message;

    @Schema(description = "返回数据")
    private T data;

    @Schema(description = "时间戳")
    private long timestamp;

    @Schema(description = "链路追踪ID（用于日志追踪和问题排查）")
    private String traceId;

    public Result() {
        this.timestamp = System.currentTimeMillis();
        this.traceId = IdUtil.fastSimpleUUID();
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
        this.traceId = IdUtil.fastSimpleUUID();
    }

    // ==================== 成功返回 ====================

    /**
     * 成功返回（无数据）
     */
    public static <T> Result<T> success() {
        return new Result<>(SUCCESS_CODE, "操作成功", null);
    }

    /**
     * 成功返回（有数据）
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(SUCCESS_CODE, "操作成功", data);
    }

    /**
     * 成功返回（自定义消息）
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(SUCCESS_CODE, message, data);
    }

    // ==================== 失败返回 ====================

    /**
     * 失败返回（默认消息）
     */
    public static <T> Result<T> fail() {
        return new Result<>(FAIL_CODE, "操作失败", null);
    }

    /**
     * 失败返回（自定义消息）
     */
    public static <T> Result<T> fail(String message) {
        return new Result<>(FAIL_CODE, message, null);
    }

    /**
     * 失败返回（自定义状态码和消息）
     */
    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null);
    }

    // ==================== 常用状态码 ====================

    /**
     * 参数错误
     */
    public static <T> Result<T> paramError(String message) {
        return new Result<>(400, message, null);
    }

    /**
     * 未授权
     */
    public static <T> Result<T> unauthorized() {
        return new Result<>(401, "未授权，请登录", null);
    }

    /**
     * 无权限
     */
    public static <T> Result<T> forbidden() {
        return new Result<>(403, "无权限访问", null);
    }

    /**
     * 资源不存在
     */
    public static <T> Result<T> notFound(String message) {
        return new Result<>(404, message, null);
    }

    /**
     * 服务器内部错误
     */
    public static <T> Result<T> serverError(String message) {
        return new Result<>(500, message, null);
    }
}