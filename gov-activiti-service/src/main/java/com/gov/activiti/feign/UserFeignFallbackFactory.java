package com.gov.activiti.feign;

import com.gov.common.result.Result;
import com.gov.common.sentinel.SentinelBlockHandler;
import com.gov.common.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserFeignFallbackFactory implements FallbackFactory<UserFeignClient> {

    @Override
    public UserFeignClient create(Throwable cause) {
        log.error("[Feign] 调用 gov-user-service 失败", cause);
        return id -> SentinelBlockHandler.<UserVO>blockHandler("user-service", "getById");
    }
}
