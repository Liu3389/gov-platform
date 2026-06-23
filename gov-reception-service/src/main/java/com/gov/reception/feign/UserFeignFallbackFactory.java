package com.gov.reception.feign;

import com.gov.common.result.Result;
import com.gov.common.sentinel.SentinelBlockHandler;
import com.gov.common.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 用户服务 Feign 降级工厂
 */
@Slf4j
@Component
public class UserFeignFallbackFactory implements FallbackFactory<UserFeignClient> {

    @Override
    public UserFeignClient create(Throwable cause) {
        log.error("[Feign] 调用 gov-user-service 失败", cause);
        return new UserFeignClient() {
            @Override
            public Result<UserVO> getById(Long id) {
                return SentinelBlockHandler.<UserVO>blockHandler("user-service", "getById");
            }

            @Override
            public Result<UserVO> getByUsername(String username) {
                return SentinelBlockHandler.<UserVO>blockHandler("user-service", "getByUsername");
            }
        };
    }
}
