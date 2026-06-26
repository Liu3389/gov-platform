package com.gov.reception.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.reception.entity.MaterialEntity;
import com.gov.reception.mapper.MaterialMapper;
import com.gov.reception.service.MaterialService;
import com.gov.reception.vo.MaterialVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 申报材料Service实现
 */
@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, MaterialEntity> implements MaterialService {

    @Override
    public List<MaterialVO> listByRecordId(Long recordId) {
        LambdaQueryWrapper<MaterialEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MaterialEntity::getRecordId, recordId);
        wrapper.orderByDesc(MaterialEntity::getCreateTime);

        List<MaterialEntity> list = this.list(wrapper);
        return list.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void verifyMaterial(Long id, Integer verifyStatus, String verifyRemark, Long verifyBy) {
        MaterialEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "材料不存在");
        }

        entity.setVerifyStatus(verifyStatus);
        entity.setVerifyRemark(verifyRemark);
        entity.setVerifyBy(verifyBy);
        entity.setVerifyTime(LocalDateTime.now());
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatchByRecordId(Long recordId, List<MaterialEntity> materials) {
        materials.forEach(m -> m.setRecordId(recordId));
        this.saveBatch(materials);
    }

    /**
     * Entity转VO
     */
    private MaterialVO convertToVO(MaterialEntity entity) {
        MaterialVO vo = new MaterialVO();
        BeanUtil.copyProperties(entity, vo);
        vo.setVerifyStatusDesc(getVerifyStatusDesc(entity.getVerifyStatus()));
        return vo;
    }

    /**
     * 获取审核状态描述
     */
    private String getVerifyStatusDesc(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "待审核";
            case 1: return "合格";
            case 2: return "不合格";
            case 3: return "缺失";
            default: return "未知";
        }
    }
}
