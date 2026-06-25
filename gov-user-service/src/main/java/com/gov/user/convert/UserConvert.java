package com.gov.user.convert;

import com.gov.common.vo.UserVO;
import com.gov.user.dto.DeptDTO;
import com.gov.user.dto.MenuDTO;
import com.gov.user.dto.RoleDTO;
import com.gov.user.dto.UserDTO;
import com.gov.user.entity.DeptEntity;
import com.gov.user.entity.MenuEntity;
import com.gov.user.entity.RoleEntity;
import com.gov.user.entity.UserEntity;
import com.gov.user.vo.DeptVO;
import com.gov.user.vo.MenuVO;
import com.gov.user.vo.RoleVO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConvert {

    public static UserEntity toEntity(UserDTO dto) {
        if (dto == null) return null;
        UserEntity entity = new UserEntity();
        entity.setId(dto.getId());
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setPhone(dto.getPhone());
        entity.setRealName(dto.getRealName());
        entity.setEmail(dto.getEmail());
        entity.setAvatar(dto.getAvatar());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    public static UserVO toVO(UserEntity entity) {
        if (entity == null) return null;
        UserVO vo = new UserVO();
        vo.setId(entity.getId());
        vo.setUsername(entity.getUsername());
        vo.setPhone(entity.getPhone());
        vo.setRealName(entity.getRealName());
        vo.setEmail(entity.getEmail());
        vo.setAvatar(entity.getAvatar());
        vo.setStatus(entity.getStatus());
        vo.setLastLoginTime(entity.getLastLoginTime());
        vo.setCreateTime(entity.getCreateTime());
        return vo;
    }

    public static List<UserVO> toVOList(List<UserEntity> entities) {
        if (entities == null) return null;
        return entities.stream()
                .map(UserConvert::toVO)
                .collect(Collectors.toList());
    }

    public static DeptEntity toEntity(DeptDTO dto) {
        if (dto == null) return null;
        DeptEntity entity = new DeptEntity();
        entity.setId(dto.getId());
        entity.setDeptName(dto.getDeptName());
        entity.setDeptCode(dto.getDeptCode());
        entity.setParentId(dto.getParentId());
        entity.setDeptType(dto.getDeptType());
        entity.setLeaderId(dto.getLeaderId());
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
        entity.setSort(dto.getSort());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    public static DeptVO toVO(DeptEntity entity) {
        if (entity == null) return null;
        DeptVO vo = new DeptVO();
        vo.setId(entity.getId());
        vo.setDeptName(entity.getDeptName());
        vo.setDeptCode(entity.getDeptCode());
        vo.setParentId(entity.getParentId());
        vo.setDeptType(entity.getDeptType());
        vo.setLeaderId(entity.getLeaderId());
        vo.setPhone(entity.getPhone());
        vo.setAddress(entity.getAddress());
        vo.setSort(entity.getSort());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        vo.setUpdateTime(entity.getUpdateTime());
        return vo;
    }

    public static List<DeptVO> toDeptVOList(List<DeptEntity> entities) {
        if (entities == null) return null;
        return entities.stream()
                .map(UserConvert::toVO)
                .collect(Collectors.toList());
    }

    public static RoleEntity toEntity(RoleDTO dto) {
        if (dto == null) return null;
        RoleEntity entity = new RoleEntity();
        entity.setId(dto.getId());
        entity.setRoleName(dto.getRoleName());
        entity.setRoleCode(dto.getRoleCode());
        entity.setStatus(dto.getStatus());
        entity.setRemark(dto.getRemark());
        return entity;
    }

    public static RoleVO toVO(RoleEntity entity) {
        if (entity == null) return null;
        RoleVO vo = new RoleVO();
        vo.setId(entity.getId());
        vo.setRoleName(entity.getRoleName());
        vo.setRoleCode(entity.getRoleCode());
        vo.setStatus(entity.getStatus());
        vo.setRemark(entity.getRemark());
        vo.setCreateTime(entity.getCreateTime());
        vo.setUpdateTime(entity.getUpdateTime());
        return vo;
    }

    public static List<RoleVO> toRoleVOList(List<RoleEntity> entities) {
        if (entities == null) return null;
        return entities.stream()
                .map(UserConvert::toVO)
                .collect(Collectors.toList());
    }

    public static MenuEntity toEntity(MenuDTO dto) {
        if (dto == null) return null;
        MenuEntity entity = new MenuEntity();
        entity.setId(dto.getId());
        entity.setMenuName(dto.getMenuName());
        entity.setMenuCode(dto.getMenuCode());
        entity.setMenuUrl(dto.getMenuUrl());
        entity.setMenuType(dto.getMenuType());
        entity.setParentId(dto.getParentId());
        entity.setIcon(dto.getIcon());
        entity.setSort(dto.getSort());
        entity.setVisible(dto.getVisible());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    public static MenuVO toVO(MenuEntity entity) {
        if (entity == null) return null;
        MenuVO vo = new MenuVO();
        vo.setId(entity.getId());
        vo.setMenuName(entity.getMenuName());
        vo.setMenuCode(entity.getMenuCode());
        vo.setMenuUrl(entity.getMenuUrl());
        vo.setMenuType(entity.getMenuType());
        vo.setParentId(entity.getParentId());
        vo.setIcon(entity.getIcon());
        vo.setSort(entity.getSort());
        vo.setVisible(entity.getVisible());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        vo.setUpdateTime(entity.getUpdateTime());
        return vo;
    }

    public static List<MenuVO> toMenuVOList(List<MenuEntity> entities) {
        if (entities == null) return null;
        return entities.stream()
                .map(UserConvert::toVO)
                .collect(Collectors.toList());
    }
}
