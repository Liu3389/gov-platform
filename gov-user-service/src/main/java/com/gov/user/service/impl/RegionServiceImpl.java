package com.gov.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.user.dto.RegionDTO;
import com.gov.user.entity.RegionEntity;
import com.gov.user.mapper.RegionMapper;
import com.gov.user.service.RegionService;
import com.gov.user.vo.RegionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl extends ServiceImpl<RegionMapper, RegionEntity> implements RegionService {

    @Override
    public List<RegionVO> listAllVO() {
        return this.lambdaQuery()
                .eq(RegionEntity::getDeleted, 0)
                .eq(RegionEntity::getStatus, 1)
                .orderByAsc(RegionEntity::getSort)
                .list()
                .stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RegionVO> listVOByParentCode(String parentCode) {
        return this.lambdaQuery()
                .eq(RegionEntity::getDeleted, 0)
                .eq(RegionEntity::getParentCode, parentCode)
                .eq(RegionEntity::getStatus, 1)
                .orderByAsc(RegionEntity::getSort)
                .list()
                .stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public RegionVO getVOById(Long id) {
        RegionEntity entity = this.getById(id);
        if (entity == null) {
            throw BusinessException.notFound("区划不存在");
        }
        return toVO(entity);
    }

    @Override
    public RegionVO getVOByCode(String regionCode) {
        RegionEntity entity = this.lambdaQuery()
                .eq(RegionEntity::getDeleted, 0)
                .eq(RegionEntity::getRegionCode, regionCode)
                .one();
        if (entity == null) {
            throw BusinessException.notFound("区划不存在");
        }
        return toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRegion(RegionDTO dto) {
        RegionEntity entity = new RegionEntity();
        BeanUtils.copyProperties(dto, entity);
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRegion(RegionDTO dto) {
        RegionEntity exist = this.getById(dto.getId());
        if (exist == null) {
            throw BusinessException.notFound("区划不存在");
        }
        RegionEntity entity = new RegionEntity();
        BeanUtils.copyProperties(dto, entity);
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRegion(Long id) {
        RegionEntity entity = this.getById(id);
        if (entity == null) {
            throw BusinessException.notFound("区划不存在");
        }
        this.removeById(id);
    }

    private RegionVO toVO(RegionEntity entity) {
        RegionVO vo = new RegionVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}
