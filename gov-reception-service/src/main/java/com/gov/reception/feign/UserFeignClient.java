package com.gov.reception.feign;

import com.gov.common.result.Result;
import com.gov.common.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户服务 Feign 客户端
 * gov-reception-service → gov-user-service
 */
@FeignClient(name = "gov-user-service", path = "/user", fallbackFactory = UserFeignFallbackFactory.class)
public interface UserFeignClient {

    @GetMapping("/{id}")
    Result<UserVO> getById(@PathVariable("id") Long id);

    @GetMapping("/byUsername")
    Result<UserVO> getByUsername(@RequestParam("username") String username);
}
