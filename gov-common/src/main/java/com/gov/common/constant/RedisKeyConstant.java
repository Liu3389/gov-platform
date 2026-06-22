package com.gov.common.constant;

/**
 * Redis Key 常量
 * 所有 Redis Key 必须在此定义
 */
public class RedisKeyConstant {

    /** Key 前缀 */
    public static final String PREFIX = "gov:";

    // ==================== 用户相关 ====================

    /** 用户 Token */
    public static final String USER_TOKEN = PREFIX + "user:token:";

    /** 用户信息缓存 */
    public static final String USER_INFO = PREFIX + "user:info:";

    /** 用户权限列表 */
    public static final String USER_PERMISSION = PREFIX + "user:permission:";

    // ==================== 办件相关 ====================

    /** 办件号序号 */
    public static final String APPLY_SEQ = PREFIX + "apply:seq:";

    /** 办件详情缓存 */
    public static final String APPLY_INFO = PREFIX + "apply:info:";

    // ==================== 流程相关 ====================

    /** 流程实例缓存 */
    public static final String PROCESS_INSTANCE = PREFIX + "process:instance:";

    /** 待办任务计数 */
    public static final String TODO_COUNT = PREFIX + "todo:count:";

    // ==================== 证照相关 ====================

    /** 证照详情缓存 */
    public static final String LICENSE_INFO = PREFIX + "license:info:";

    /** 证照核验记录 */
    public static final String LICENSE_VERIFY = PREFIX + "license:verify:";

    // ==================== 消息相关 ====================

    /** 未读消息计数 */
    public static final String UNREAD_COUNT = PREFIX + "message:unread:";

    // ==================== 统计相关 ====================

    /** 效能统计缓存 */
    public static final String EFFICIENCY_STAT = PREFIX + "monitor:efficiency:";

    /** 部门办件统计 */
    public static final String DEPT_STAT = PREFIX + "monitor:dept:";

    // ==================== 分布式锁 ====================

    /** 办件号生成锁 */
    public static final String LOCK_APPLY_NO = PREFIX + "lock:apply:no:";

    /** 证照生成锁 */
    public static final String LOCK_LICENSE = PREFIX + "lock:license:";

    // ==================== 方法 ====================

    /**
     * 构建用户 Token Key
     */
    public static String userTokenKey(Long userId) {
        return USER_TOKEN + userId;
    }

    /**
     * 构建用户信息 Key
     */
    public static String userInfoKey(Long userId) {
        return USER_INFO + userId;
    }

    /**
     * 构建办件号序号 Key
     */
    public static String applySeqKey(String year, String deptCode) {
        return APPLY_SEQ + year + deptCode;
    }
}