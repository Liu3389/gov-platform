package com.gov.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
}