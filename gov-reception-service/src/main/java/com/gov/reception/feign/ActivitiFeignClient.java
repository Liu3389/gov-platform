package com.gov.reception.feign;

import com.gov.common.result.Result;
import com.gov.common.vo.WorkflowTaskVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 审批流服务 Feign 客户端
 * gov-reception-service → gov-activiti-service
 */
@FeignClient(name = "gov-activiti-service", path = "/workflow", fallbackFactory = ActivitiFeignFallbackFactory.class)
public interface ActivitiFeignClient {

    @PostMapping("/start")
    Result<WorkflowTaskVO> startProcess(@RequestBody Object startProcessDTO);
}
