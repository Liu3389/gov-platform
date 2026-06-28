package com.gov.datashare.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.datashare.dto.SubscribeDTO;
import com.gov.datashare.entity.SubscribeEntity;
import com.gov.datashare.vo.SubscribeVO;

public interface SubscribeService extends IService<SubscribeEntity> {

    PageResult<SubscribeVO> pageQueryVO(Long pageNum, Long pageSize, Long apiId, String status);

    SubscribeVO getVOById(Long id);

    void addSubscribe(SubscribeDTO dto);

    void updateSubscribe(SubscribeDTO dto);

    void deleteSubscribe(Long id);
}
