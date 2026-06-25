package com.gov.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.user.dto.DictDTO;
import com.gov.user.entity.DictEntity;
import com.gov.user.mapper.DictMapper;
import com.gov.user.service.DictService;
import com.gov.user.vo.DictVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DictServiceImpl extends ServiceImpl<DictMapper, DictEntity> implements DictService {

    @Override
    public List<DictVO> listVOByType(String dictType) {
        return this.lambdaQuery()
                .eq(DictEntity::getDeleted, 0)
                .eq(DictEntity::getDictType, dictType)
                .eq(DictEntity::getStatus, 1)
                .orderByAsc(DictEntity::getSort)
                .list()
                .stream()
                .map(this::toVO)
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<DictVO> pageQueryVO(Long pageNum, Long pageSize, String dictType) {
        LambdaQueryWrapper<DictEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DictEntity::getDeleted, 0);
        if (StringUtils.hasText(dictType)) {
            wrapper.eq(DictEntity::getDictType, dictType);
        }
        wrapper.orderByAsc(DictEntity::getSort);
        var page = this.page(new Page<>(pageNum, pageSize), wrapper);
        return PageResult.of(page.getRecords().stream().map(this::toVO).collect(Collectors.toList()), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public DictVO getVOById(Long id) {
        DictEntity entity = this.getById(id);
        if (entity == null) {
            throw BusinessException.notFound("字典不存在");
        }
        return toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDict(DictDTO dto) {
        DictEntity entity = new DictEntity();
        BeanUtils.copyProperties(dto, entity);
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDict(DictDTO dto) {
        DictEntity exist = this.getById(dto.getId());
        if (exist == null) {
            throw BusinessException.notFound("字典不存在");
        }
        DictEntity entity = new DictEntity();
        BeanUtils.copyProperties(dto, entity);
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDict(Long id) {
        DictEntity entity = this.getById(id);
        if (entity == null) {
            throw BusinessException.notFound("字典不存在");
        }
        this.removeById(id);
    }

    private DictVO toVO(DictEntity entity) {
        DictVO vo = new DictVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}
