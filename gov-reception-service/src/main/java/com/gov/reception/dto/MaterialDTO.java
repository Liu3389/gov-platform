package com.gov.reception.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 材料DTO
 */
@Data
@Schema(description = "申报材料请求")
public class MaterialDTO {

    @Schema(description = "材料名称")
    private String materialName;

    @Schema(description = "材料类型")
    private String materialType;

    @Schema(description = "文件存储路径")
    private String fileUrl;

    @Schema(description = "文件大小（字节）")
    private Long fileSize;

    @Schema(description = "文件类型（jpg/png/pdf）")
    private String fileType;

    @Schema(description = "是否必须：0否 1是")
    private Integer isRequired;

    /**
     * 转为 Entity
     */
    public com.gov.reception.entity.MaterialEntity toEntity() {
        com.gov.reception.entity.MaterialEntity entity = new com.gov.reception.entity.MaterialEntity();
        entity.setMaterialName(this.materialName);
        entity.setMaterialType(this.materialType);
        entity.setFileUrl(this.fileUrl);
        entity.setFileSize(this.fileSize);
        entity.setFileType(this.fileType);
        entity.setIsRequired(this.isRequired);
        return entity;
    }
}
