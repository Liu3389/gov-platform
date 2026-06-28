package com.gov.datashare.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.datashare.dto.CatalogDTO;
import com.gov.datashare.entity.CatalogEntity;
import com.gov.datashare.mapper.CatalogMapper;
import com.gov.datashare.service.CatalogService;
import com.gov.datashare.vo.CatalogVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogServiceImpl extends ServiceImpl<CatalogMapper, CatalogEntity> implements CatalogService {

    @Override
    public PageResult<CatalogVO> pageQueryVO(Long pageNum, Long pageSize, String keyword, String status) {
        LambdaQueryWrapper<CatalogEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CatalogEntity::getDeleted, 0);
        wrapper.like(StrUtil.isNotBlank(keyword), CatalogEntity::getCatalogName, keyword);
        wrapper.eq(StrUtil.isNotBlank(status), CatalogEntity::getStatus, status);
        wrapper.orderByDesc(CatalogEntity::getCreateTime);
        Page<CatalogEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<CatalogVO> voList = page.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public CatalogVO getVOById(Long id) {
        CatalogEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "目录不存在");
        }
        return toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCatalog(CatalogDTO dto) {
        CatalogEntity entity = dto.toEntity();
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCatalog(CatalogDTO dto) {
        CatalogEntity entity = this.getById(dto.getId());
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "目录不存在");
        }
        BeanUtil.copyProperties(dto, entity, "id", "catalogCode", "createTime", "createBy", "deleted");
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCatalog(Long id) {
        CatalogEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "目录不存在");
        }
        this.removeById(id);
    }

    private CatalogVO toVO(CatalogEntity entity) {
        CatalogVO vo = new CatalogVO();
        BeanUtil.copyProperties(entity, vo);
        return vo;
    }
}
