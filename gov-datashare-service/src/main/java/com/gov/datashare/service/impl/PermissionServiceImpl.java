package com.gov.datashare.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.datashare.dto.PermissionDTO;
import com.gov.datashare.entity.PermissionEntity;
import com.gov.datashare.mapper.PermissionMapper;
import com.gov.datashare.service.PermissionService;
import com.gov.datashare.vo.PermissionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, PermissionEntity> implements PermissionService {

    @Override
    public PageResult<PermissionVO> pageQueryVO(Long pageNum, Long pageSize, Long apiId, String status) {
        LambdaQueryWrapper<PermissionEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PermissionEntity::getDeleted, 0);
        wrapper.eq(apiId != null, PermissionEntity::getApiId, apiId);
        wrapper.eq(StrUtil.isNotBlank(status), PermissionEntity::getStatus, status);
        wrapper.orderByDesc(PermissionEntity::getCreateTime);
        Page<PermissionEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<PermissionVO> voList = page.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public PermissionVO getVOById(Long id) {
        PermissionEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "权限记录不存在");
        }
        return toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPermission(PermissionDTO dto) {
        PermissionEntity entity = dto.toEntity();
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePermission(PermissionDTO dto) {
        PermissionEntity entity = this.getById(dto.getId());
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "权限记录不存在");
        }
        BeanUtil.copyProperties(dto, entity, "id", "createTime", "createBy", "deleted");
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePermission(Long id) {
        PermissionEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "权限记录不存在");
        }
        this.removeById(id);
    }

    private PermissionVO toVO(PermissionEntity entity) {
        PermissionVO vo = new PermissionVO();
        BeanUtil.copyProperties(entity, vo);
        return vo;
    }
}
