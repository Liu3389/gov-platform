package com.gov.open.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.open.dto.CatalogDTO;
import com.gov.open.entity.CatalogEntity;
import com.gov.open.mapper.CatalogMapper;
import com.gov.open.service.CatalogService;
import com.gov.open.vo.CatalogVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 公开目录Service实现
 */
@Service
@RequiredArgsConstructor
public class CatalogServiceImpl extends ServiceImpl<CatalogMapper, CatalogEntity> implements CatalogService {

    @Override
    public List<CatalogVO> getCatalogTree(Integer catalogType) {
        LambdaQueryWrapper<CatalogEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CatalogEntity::getDeleted, 0);
        wrapper.eq(catalogType != null, CatalogEntity::getCatalogType, String.valueOf(catalogType));
        wrapper.eq(CatalogEntity::getStatus, "1");
        wrapper.orderByAsc(CatalogEntity::getSort);

        List<CatalogEntity> allCatalogs = this.list(wrapper);

        // 构建树结构
        List<CatalogVO> allVOs = allCatalogs.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return buildTree(allVOs, 0L);
    }

    @Override
    public CatalogVO getVOById(Long id) {
        CatalogEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            return null;
        }
        return convertToVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCatalog(CatalogDTO dto) {
        // 检查编码是否重复
        LambdaQueryWrapper<CatalogEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CatalogEntity::getCatalogCode, dto.getCatalogCode());
        if (this.count(wrapper) > 0) {
            throw new BusinessException(400, "目录编码已存在");
        }

        CatalogEntity entity = new CatalogEntity();
        BeanUtil.copyProperties(dto, entity);

        if (entity.getParentId() == null) {
            entity.setParentId(0L);
        }
        if (entity.getCatalogLevel() == null) {
            entity.setCatalogLevel(1);
        }
        if (entity.getSort() == null) {
            entity.setSort(0);
        }
        if (entity.getStatus() == null) {
            entity.setStatus("1");
        }
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());

        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCatalog(CatalogDTO dto, Long id) {
        CatalogEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "目录不存在");
        }

        // 如果修改了编码，检查是否重复
        if (!entity.getCatalogCode().equals(dto.getCatalogCode())) {
            LambdaQueryWrapper<CatalogEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(CatalogEntity::getCatalogCode, dto.getCatalogCode());
            wrapper.ne(CatalogEntity::getId, id);
            if (this.count(wrapper) > 0) {
                throw new BusinessException(400, "目录编码已存在");
            }
        }

        BeanUtil.copyProperties(dto, entity, "id", "createTime", "createBy");
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCatalog(Long id) {
        CatalogEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "目录不存在");
        }

        // 检查是否有子目录
        LambdaQueryWrapper<CatalogEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CatalogEntity::getParentId, id);
        if (this.count(wrapper) > 0) {
            throw new BusinessException(400, "该目录下有子目录，无法删除");
        }

        this.removeById(id);
    }

    /**
     * 构建树结构
     */
    private List<CatalogVO> buildTree(List<CatalogVO> allVOs, Long parentId) {
        return allVOs.stream()
                .filter(vo -> parentId.equals(vo.getParentId()))
                .peek(vo -> vo.setChildren(buildTree(allVOs, vo.getId())))
                .collect(Collectors.toList());
    }

    /**
     * Entity转VO
     */
    private CatalogVO convertToVO(CatalogEntity entity) {
        CatalogVO vo = new CatalogVO();
        BeanUtil.copyProperties(entity, vo);
        vo.setCatalogTypeDesc(getCatalogTypeDesc(entity.getCatalogType()));
        vo.setChildren(new ArrayList<>());
        return vo;
    }

    private String getCatalogTypeDesc(String type) {
        if (type == null) return "未知";
        switch (type) {
            case "1": return "政府信息公开";
            case "2": return "政务动态";
            case "3": return "重点领域";
            default: return "未知";
        }
    }
}
