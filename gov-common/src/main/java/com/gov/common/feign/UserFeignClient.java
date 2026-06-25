package com.gov.common.feign;

import com.gov.common.result.Result;
import com.gov.common.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户服务 Feign 客户端（跨服务共享）
 * 其他服务通过此接口调用 gov-user-service 的用户查询能力
 */
@FeignClient(
        name = "gov-user-service",
        path = "/user",
        fallbackFactory = UserFeignFallbackFactory.class
)
public interface UserFeignClient {

    @GetMapping("/{id}")
    Result<UserVO> getById(@PathVariable("id") Long id);

    @GetMapping("/byUsername")
    Result<UserVO> getByUsername(@RequestParam("username") String username);
}
