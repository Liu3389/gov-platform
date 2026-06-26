package com.gov.reception.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 表单数据实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_reception_form_data")
@Schema(description = "表单数据")
public class FormDataEntity extends BaseEntity {

    @Schema(description = "办件ID")
    private Long recordId;

    @Schema(description = "表单数据（JSON格式）")
    private String formData;

    @Schema(description = "表单版本号")
    private Integer formVersion;
}
