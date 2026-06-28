package com.gov.datashare.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.datashare.dto.LogDTO;
import com.gov.datashare.entity.LogEntity;
import com.gov.datashare.vo.LogVO;

public interface LogService extends IService<LogEntity> {

    PageResult<LogVO> pageQueryVO(Long pageNum, Long pageSize, Long apiId, String callResult);

    LogVO getVOById(Long id);

    void addLog(LogDTO dto);

    void updateLog(LogDTO dto);

    void deleteLog(Long id);
}
