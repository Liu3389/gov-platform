package com.gov.reception.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.reception.dto.RecordAcceptDTO;
import com.gov.reception.dto.RecordQueryDTO;
import com.gov.reception.dto.RecordSubmitDTO;
import com.gov.reception.entity.RecordEntity;
import com.gov.reception.vo.RecordProgressVO;
import com.gov.reception.vo.RecordVO;

/**
 * 办件Service
 */
public interface RecordService extends IService<RecordEntity> {

    /**
     * 提交办件
     */
    RecordVO submitRecord(RecordSubmitDTO dto, Long userId);

    /**
     * 受理登记
     */
    void acceptRecord(RecordAcceptDTO dto, Long operatorId, String operatorName);

    /**
     * 分页查询办件VO
     */
    PageResult<RecordVO> pageQueryVO(Long pageNum, Long pageSize, RecordQueryDTO queryDTO);

    /**
     * 根据ID查询办件VO
     */
    RecordVO getVOById(Long id);

    /**
     * 查询办件进度
     */
    RecordProgressVO getRecordProgress(Long id);

    /**
     * 生成办件号
     */
    String generateApplyNo(Long deptId);
}
