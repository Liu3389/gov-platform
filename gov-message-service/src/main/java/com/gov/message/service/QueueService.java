package com.gov.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.message.dto.QueueDTO;
import com.gov.message.entity.QueueEntity;
import com.gov.message.vo.QueueVO;

public interface QueueService extends IService<QueueEntity> {

    PageResult<QueueVO> pageQueryVO(Long pageNum, Long pageSize, Integer queueStatus, Integer priority);

    QueueVO getVOById(Long id);

    void addQueue(QueueDTO dto);

    void updateQueue(QueueDTO dto);

    void deleteQueue(Long id);
}
