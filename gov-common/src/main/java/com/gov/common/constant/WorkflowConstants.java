package com.gov.common.constant;

/**
 * 工作流常量
 * 所有流程变量必须使用此常量
 */
public class WorkflowConstants {

    // ==================== 流程变量名 ====================

    /** 办件号 */
    public static final String VAR_APPLY_NO = "applyNo";

    /** 申请人ID */
    public static final String VAR_USER_ID = "userId";

    /** 部门ID */
    public static final String VAR_DEPT_ID = "deptId";

    /** 事项ID */
    public static final String VAR_ITEM_ID = "itemId";

    /** 审批结果（1通过 2驳回 3转办） */
    public static final String VAR_APPROVAL_RESULT = "approvalResult";

    /** 审批意见 */
    public static final String VAR_APPROVAL_OPINION = "approvalOpinion";

    /** 下一审批人 */
    public static final String VAR_NEXT_ASSIGNEE = "nextAssignee";

    /** 会签部门列表 */
    public static final String VAR_DEPT_LIST = "deptList";

    /** 会签部门领导 */
    public static final String VAR_DEPT_LEADER = "deptLeader";

    /** 是否全部通过 */
    public static final String VAR_ALL_APPROVED = "allApproved";

    // ==================== 流程 Key ====================

    /** 单部门审批流程 */
    public static final String PROCESS_SINGLE_DEPT = "apply_approval_v1";

    /** 多部门会签流程 */
    public static final String PROCESS_MULTI_DEPT = "apply_countersign_v1";

    /** 证照发放流程 */
    public static final String PROCESS_LICENSE_ISSUE = "license_issue_v1";

    /** 投诉处理流程 */
    public static final String PROCESS_COMPLAINT = "complaint_handle_v1";

    // ==================== 审批结果 ====================

    /** 通过 */
    public static final int APPROVAL_PASS = 1;

    /** 驳回 */
    public static final int APPROVAL_REJECT = 2;

    /** 转办 */
    public static final int APPROVAL_TRANSFER = 3;

    /** 退回 */
    public static final int APPROVAL_RETURN = 4;

    // ==================== 预警类型 ====================

    /** 黄牌（即将超期） */
    public static final String WARNING_YELLOW = "YELLOW_CARD";

    /** 红牌（已超期） */
    public static final String WARNING_RED = "RED_CARD";
}