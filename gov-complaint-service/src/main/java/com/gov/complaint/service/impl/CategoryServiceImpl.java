package com.gov.complaint.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.complaint.dto.CategoryDTO;
import com.gov.complaint.entity.CategoryEntity;
import com.gov.complaint.mapper.CategoryMapper;
import com.gov.complaint.service.CategoryService;
import com.gov.complaint.vo.CategoryVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryEntity> implements CategoryService {

    @Override
    public PageResult<CategoryVO> pageQueryVO(Long pageNum, Long pageSize, String keyword, String status) {
        LambdaQueryWrapper<CategoryEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryEntity::getDeleted, 0);
        wrapper.like(StrUtil.isNotBlank(keyword), CategoryEntity::getCategoryName, keyword);
        wrapper.eq(StrUtil.isNotBlank(status), CategoryEntity::getStatus, status);
        wrapper.orderByAsc(CategoryEntity::getSort);
        Page<CategoryEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<CategoryVO> voList = page.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public CategoryVO getVOById(Long id) {
        CategoryEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "分类不存在");
        }
        return toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCategory(CategoryDTO dto) {
        CategoryEntity entity = dto.toEntity();
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(CategoryDTO dto) {
        CategoryEntity entity = this.getById(dto.getId());
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "分类不存在");
        }
        BeanUtil.copyProperties(dto, entity, "id", "categoryCode", "createTime", "createBy", "deleted");
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Long id) {
        CategoryEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "分类不存在");
        }
        this.removeById(id);
    }

    private CategoryVO toVO(CategoryEntity entity) {
        CategoryVO vo = new CategoryVO();
        BeanUtil.copyProperties(entity, vo);
        return vo;
    }
}
