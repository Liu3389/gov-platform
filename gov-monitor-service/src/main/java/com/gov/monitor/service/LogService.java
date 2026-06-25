package com.gov.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.event.OperateLogEvent;
import com.gov.common.result.PageResult;
import com.gov.monitor.dto.LogDTO;
import com.gov.monitor.entity.LogEntity;
import com.gov.monitor.vo.LogVO;

public interface LogService extends IService<LogEntity> {

    PageResult<LogVO> pageQueryVO(Long pageNum, Long pageSize);

    LogVO getVOById(Long id);

    void addLog(LogDTO dto);

    void deleteLog(Long id);

    void recordLog(OperateLogEvent event);
}
