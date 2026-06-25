package com.gov.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.user.convert.UserConvert;
import com.gov.user.dto.MenuDTO;
import com.gov.user.entity.MenuEntity;
import com.gov.user.entity.RoleMenuEntity;
import com.gov.user.entity.UserRoleEntity;
import com.gov.user.mapper.MenuMapper;
import com.gov.user.mapper.RoleMenuMapper;
import com.gov.user.mapper.UserRoleMapper;
import com.gov.user.service.MenuService;
import com.gov.user.vo.MenuVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuEntity> implements MenuService {

    private final UserRoleMapper userRoleMapper;
    private final RoleMenuMapper roleMenuMapper;

    @Override
    public List<MenuEntity> listAll() {
        return this.lambdaQuery()
                .eq(MenuEntity::getDeleted, 0)
                .eq(MenuEntity::getStatus, 1)
                .orderByAsc(MenuEntity::getSort)
                .list();
    }

    @Override
    public List<MenuVO> listAllVO() {
        List<MenuEntity> entities = listAll();
        return UserConvert.toMenuVOList(entities);
    }

    @Override
    public List<MenuEntity> listMenusByUserId(Long userId) {
        // 1. 查用户角色
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

        // 2. 查角色菜单
        LambdaQueryWrapper<RoleMenuEntity> rmWrapper = new LambdaQueryWrapper<>();
        rmWrapper.eq(RoleMenuEntity::getDeleted, 0)
                .in(RoleMenuEntity::getRoleId, roleIds);
        List<RoleMenuEntity> roleMenus = roleMenuMapper.selectList(rmWrapper);
        if (roleMenus.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Long> menuIds = roleMenus.stream()
                .map(RoleMenuEntity::getMenuId)
                .collect(Collectors.toSet());

        // 3. 查菜单
        return this.lambdaQuery()
                .eq(MenuEntity::getDeleted, 0)
                .eq(MenuEntity::getStatus, 1)
                .in(MenuEntity::getId, menuIds)
                .orderByAsc(MenuEntity::getSort)
                .list();
    }

    @Override
    public List<MenuVO> listMenusVOByUserId(Long userId) {
        List<MenuEntity> entities = listMenusByUserId(userId);
        return UserConvert.toMenuVOList(entities);
    }

    @Override
    public List<MenuEntity> listMenusByRoleId(Long roleId) {
        LambdaQueryWrapper<RoleMenuEntity> rmWrapper = new LambdaQueryWrapper<>();
        rmWrapper.eq(RoleMenuEntity::getDeleted, 0)
                .eq(RoleMenuEntity::getRoleId, roleId);
        List<RoleMenuEntity> roleMenus = roleMenuMapper.selectList(rmWrapper);
        if (roleMenus.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> menuIds = roleMenus.stream()
                .map(RoleMenuEntity::getMenuId)
                .collect(Collectors.toList());
        return this.lambdaQuery()
                .eq(MenuEntity::getDeleted, 0)
                .in(MenuEntity::getId, menuIds)
                .orderByAsc(MenuEntity::getSort)
                .list();
    }

    @Override
    public List<MenuVO> listMenusVOByRoleId(Long roleId) {
        List<MenuEntity> entities = listMenusByRoleId(roleId);
        return UserConvert.toMenuVOList(entities);
    }

    @Override
    public MenuVO getVOById(Long id) {
        MenuEntity entity = this.getById(id);
        if (entity == null) {
            throw BusinessException.notFound("菜单不存在");
        }
        return UserConvert.toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addMenu(MenuDTO dto) {
        MenuEntity entity = UserConvert.toEntity(dto);
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(MenuDTO dto) {
        MenuEntity exist = this.getById(dto.getId());
        if (exist == null) {
            throw BusinessException.notFound("菜单不存在");
        }
        MenuEntity entity = UserConvert.toEntity(dto);
        entity.setId(dto.getId());
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(Long id) {
        MenuEntity entity = this.getById(id);
        if (entity == null) {
            throw BusinessException.notFound("菜单不存在");
        }
        this.removeById(id);
    }
}
