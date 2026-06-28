package com.gov.message.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.message.dto.RecordDTO;
import com.gov.message.entity.RecordEntity;
import com.gov.message.vo.RecordVO;

public interface RecordService extends IService<RecordEntity> {

    PageResult<RecordVO> pageQueryVO(Long pageNum, Long pageSize, String channel, String sendStatus);

    RecordVO getVOById(Long id);

    void addRecord(RecordDTO dto);

    void updateRecord(RecordDTO dto);

    void deleteRecord(Long id);
}
