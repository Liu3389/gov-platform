package com.gov.item.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "事项信息响应")
public class ItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "事项ID")
    private Long id;

    @Schema(description = "事项编码")
    private String itemCode;

    @Schema(description = "事项名称")
    private String itemName;

    @Schema(description = "所属部门ID")
    private Long deptId;

    @Schema(description = "事项类型：1行政许可 2公共服务 3行政确认")
    private Integer itemType;

    @Schema(description = "状态：0草稿 1发布 2下架")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
