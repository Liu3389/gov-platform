package com.gov.reception.feign;

import com.gov.common.result.Result;
import com.gov.common.vo.ItemVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 事项服务 Feign 客户端
 * gov-reception-service → gov-item-service
 */
@FeignClient(name = "gov-item-service", path = "/item", fallbackFactory = ItemFeignFallbackFactory.class)
public interface ItemFeignClient {

    @GetMapping("/{id}")
    Result<ItemVO> getById(@PathVariable("id") Long id);
}
