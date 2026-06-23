package com.gov.activiti.feign;

import com.gov.common.result.Result;
import com.gov.common.sentinel.SentinelBlockHandler;
import com.gov.common.vo.LicenseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LicenseFeignFallbackFactory implements FallbackFactory<LicenseFeignClient> {

    @Override
    public LicenseFeignClient create(Throwable cause) {
        log.error("[Feign] 调用 gov-license-service 失败", cause);
        return dto -> SentinelBlockHandler.<LicenseVO>blockHandler("license-service", "generate");
    }
}
