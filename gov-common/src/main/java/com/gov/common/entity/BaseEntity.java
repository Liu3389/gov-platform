package com.gov.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体基类
 * 所有 Entity 必须继承此基类
 */
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 ID（雪花 ID，MyBatis-Plus 自动生成）
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 创建时间（自动填充）
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间（自动填充）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建人ID（自动填充）
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 更新人ID（自动填充）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /**
     * 逻辑删除：0=正常，1=已删除
     */
    @TableLogic
    private Integer deleted;
}