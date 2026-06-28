package com.gov.reception.feign;

import com.gov.common.result.Result;
import com.gov.reception.dto.StartProcessDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 审批流服务 Feign 客户端
 * gov-reception-service → gov-activiti-service
 */
@FeignClient(name = "gov-activiti-service", path = "/workflow", fallbackFactory = ActivitiFeignFallbackFactory.class)
public interface ActivitiFeignClient {

    /**
     * 启动审批流程
     * 对应接口：POST /workflow/process → 返回 Result<String>（instanceId）
     */
    @PostMapping("/process")
    Result<String> startProcess(@RequestBody StartProcessDTO startProcessDTO);
}
