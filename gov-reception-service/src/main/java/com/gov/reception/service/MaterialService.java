package com.gov.reception.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.reception.entity.MaterialEntity;
import com.gov.reception.vo.MaterialVO;

import java.util.List;

/**
 * 申报材料Service
 */
public interface MaterialService extends IService<MaterialEntity> {

    /**
     * 根据办件ID查询材料列表
     */
    List<MaterialVO> listByRecordId(Long recordId);

    /**
     * 审核材料
     */
    void verifyMaterial(Long id, Integer verifyStatus, String verifyRemark, Long verifyBy);

    /**
     * 批量保存材料
     */
    void saveBatchByRecordId(Long recordId, List<MaterialEntity> materials);
}
