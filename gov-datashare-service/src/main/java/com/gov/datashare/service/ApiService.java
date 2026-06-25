package com.gov.datashare.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.datashare.dto.ApiDTO;
import com.gov.datashare.entity.ApiEntity;
import com.gov.datashare.vo.ApiVO;

public interface ApiService extends IService<ApiEntity> {

    PageResult<ApiVO> pageQueryVO(Long pageNum, Long pageSize, String keyword, String status);

    ApiVO getVOById(Long id);

    void addApi(ApiDTO dto);

    void updateApi(ApiDTO dto);

    void deleteApi(Long id);
}
