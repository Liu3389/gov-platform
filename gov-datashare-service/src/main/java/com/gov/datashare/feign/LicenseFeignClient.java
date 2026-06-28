package com.gov.datashare.feign;

import com.gov.common.result.Result;
import com.gov.common.vo.LicenseVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 证照服务 Feign 客户端
 * 用于查询证照信息
 */
@FeignClient(name = "gov-license-service", path = "/license", fallbackFactory = LicenseFeignFallbackFactory.class)
public interface LicenseFeignClient {

    /**
     * 根据ID查询证照详情
     *
     * @param id 证照ID
     * @return 证照信息
     */
    @GetMapping("/{id}")
    Result<LicenseVO> getById(@PathVariable("id") Long id);
}
