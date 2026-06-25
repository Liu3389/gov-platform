package com.gov.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.user.convert.UserConvert;
import com.gov.user.dto.RoleDTO;
import com.gov.user.entity.RoleEntity;
import com.gov.user.entity.UserRoleEntity;
import com.gov.user.mapper.RoleMapper;
import com.gov.user.mapper.UserRoleMapper;
import com.gov.user.service.RoleService;
import com.gov.user.vo.RoleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleEntity> implements RoleService {

    private final UserRoleMapper userRoleMapper;

    @Override
    public List<RoleEntity> listAll() {
        return this.lambdaQuery()
                .eq(RoleEntity::getDeleted, 0)
                .eq(RoleEntity::getStatus, 1)
                .list();
    }

    @Override
    public List<RoleVO> listAllVO() {
        List<RoleEntity> entities = listAll();
        return UserConvert.toRoleVOList(entities);
    }

    @Override
    public PageResult<RoleEntity> pageQuery(Long pageNum, Long pageSize, String roleName) {
        LambdaQueryWrapper<RoleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleEntity::getDeleted, 0);
        if (StringUtils.hasText(roleName)) {
            wrapper.like(RoleEntity::getRoleName, roleName);
        }
        wrapper.orderByDesc(RoleEntity::getCreateTime);
        var page = this.page(new Page<>(pageNum, pageSize), wrapper);
        return PageResult.of(page.getRecords(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public PageResult<RoleVO> pageQueryVO(Long pageNum, Long pageSize, String roleName) {
        PageResult<RoleEntity> entityPage = pageQuery(pageNum, pageSize, roleName);
        List<RoleVO> voList = UserConvert.toRoleVOList(entityPage.getRecords());
        return PageResult.of(voList, entityPage.getTotal(), entityPage.getPageNum(), entityPage.getPageSize());
    }

    @Override
    public RoleEntity getByRoleCode(String roleCode) {
        return this.lambdaQuery()
                .eq(RoleEntity::getDeleted, 0)
                .eq(RoleEntity::getRoleCode, roleCode)
                .one();
    }

    @Override
    public RoleVO getVOByRoleCode(String roleCode) {
        RoleEntity entity = getByRoleCode(roleCode);
        if (entity == null) {
            throw BusinessException.notFound("角色不存在");
        }
        return UserConvert.toVO(entity);
    }

    @Override
    public RoleVO getVOById(Long id) {
        RoleEntity entity = this.getById(id);
        if (entity == null) {
            throw BusinessException.notFound("角色不存在");
        }
        return UserConvert.toVO(entity);
    }

    @Override
    public List<RoleEntity> listRolesByUserId(Long userId) {
        LambdaQueryWrapper<UserRoleEntity> urWrapper = new LambdaQueryWrapper<>();
        urWrapper.eq(UserRoleEntity::getDeleted, 0)
                .eq(UserRoleEntity::getUserId, userId);
        List<UserRoleEntity> userRoles = userRoleMapper.selectList(urWrapper);
        if (userRoles.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> roleIds = userRoles.stream()
                .map(UserRoleEntity::getRoleId)
                .collect(Collectors.toList());
        return this.lambdaQuery()
                .eq(RoleEntity::getDeleted, 0)
                .in(RoleEntity::getId, roleIds)
                .list();
    }

    @Override
    public List<RoleVO> listRolesVOByUserId(Long userId) {
        List<RoleEntity> entities = listRolesByUserId(userId);
        return UserConvert.toRoleVOList(entities);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRole(RoleDTO dto) {
        RoleEntity entity = UserConvert.toEntity(dto);
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(RoleDTO dto) {
        RoleEntity exist = this.getById(dto.getId());
        if (exist == null) {
            throw BusinessException.notFound("角色不存在");
        }
        RoleEntity entity = UserConvert.toEntity(dto);
        entity.setId(dto.getId());
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long id) {
        RoleEntity entity = this.getById(id);
        if (entity == null) {
            throw BusinessException.notFound("角色不存在");
        }
        this.removeById(id);
    }
}
