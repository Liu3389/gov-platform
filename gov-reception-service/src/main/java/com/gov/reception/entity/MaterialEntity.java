package com.gov.reception.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 申报材料实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_reception_material")
@Schema(description = "申报材料")
public class MaterialEntity extends BaseEntity {

    @Schema(description = "办件ID")
    private Long recordId;

    @Schema(description = "材料名称")
    private String materialName;

    @Schema(description = "材料类型（身份证/户口本/营业执照等）")
    private String materialType;

    @Schema(description = "文件存储路径")
    private String fileUrl;

    @Schema(description = "文件大小（字节）")
    private Long fileSize;

    @Schema(description = "文件类型（jpg/png/pdf）")
    private String fileType;

    @Schema(description = "是否必须：0否 1是")
    private Integer isRequired;

    @Schema(description = "审核状态：0待审核 1合格 2不合格 3缺失")
    private Integer verifyStatus;

    @Schema(description = "审核备注")
    private String verifyRemark;

    @Schema(description = "审核时间")
    private LocalDateTime verifyTime;

    @Schema(description = "审核人ID")
    private Long verifyBy;
}
