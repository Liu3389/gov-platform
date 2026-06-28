package com.gov.datashare.feign;

import com.gov.common.result.Result;
import com.gov.common.vo.LicenseVO;
import com.gov.common.sentinel.SentinelBlockHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 证照服务 Feign 降级工厂
 */
@Slf4j
@Component
public class LicenseFeignFallbackFactory implements FallbackFactory<LicenseFeignClient> {

    @Override
    public LicenseFeignClient create(Throwable cause) {
        log.error("[Feign] 调用 gov-license-service 失败", cause);
        return new LicenseFeignClient() {
            @Override
            public Result<LicenseVO> getById(Long id) {
                return SentinelBlockHandler.<LicenseVO>blockHandler("license-service", "getById");
            }
        };
    }
}
