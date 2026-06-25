package com.gov.activiti.feign;

import com.gov.activiti.dto.LicenseGenerateDTO;
import com.gov.common.result.Result;
import com.gov.common.vo.LicenseVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 证照服务 Feign 客户端
 * gov-activiti-service → gov-license-service (办结回调生成证照)
 */
@FeignClient(name = "gov-license-service", path = "/license", fallbackFactory = LicenseFeignFallbackFactory.class)
public interface LicenseFeignClient {

    @PostMapping("/generate")
    Result<LicenseVO> generate(@RequestBody LicenseGenerateDTO dto);
}
