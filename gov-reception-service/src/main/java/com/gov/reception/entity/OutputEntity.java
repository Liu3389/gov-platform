package com.gov.reception.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 出件记录实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_reception_output")
@Schema(description = "出件记录")
public class OutputEntity extends BaseEntity {

    @Schema(description = "办件ID")
    private Long recordId;

    @Schema(description = "出件类型：1证照 2批文 3回执")
    private Integer outputType;

    @Schema(description = "证照ID（关联gov_license库）")
    private Long licenseId;

    @Schema(description = "出件编号")
    private String outputNo;

    @Schema(description = "出件名称")
    private String outputName;

    @Schema(description = "文件路径")
    private String fileUrl;

    @Schema(description = "领取人ID")
    private Long receiverId;

    @Schema(description = "领取时间")
    private LocalDateTime receiveTime;

    @Schema(description = "状态：0待领取 1已领取 2已邮寄")
    private Integer status;
}
