package com.gov.reception.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 材料VO
 */
@Data
@Schema(description = "申报材料信息")
public class MaterialVO implements Serializable {

    @Schema(description = "材料ID")
    private Long id;

    @Schema(description = "办件ID")
    private Long recordId;

    @Schema(description = "材料名称")
    private String materialName;

    @Schema(description = "材料类型")
    private String materialType;

    @Schema(description = "文件存储路径")
    private String fileUrl;

    @Schema(description = "文件大小（字节）")
    private Long fileSize;

    @Schema(description = "文件类型")
    private String fileType;

    @Schema(description = "是否必须：0否 1是")
    private Integer isRequired;

    @Schema(description = "审核状态：0待审核 1合格 2不合格 3缺失")
    private Integer verifyStatus;

    @Schema(description = "审核状态描述")
    private String verifyStatusDesc;

    @Schema(description = "审核备注")
    private String verifyRemark;

    @Schema(description = "审核时间")
    private LocalDateTime verifyTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
