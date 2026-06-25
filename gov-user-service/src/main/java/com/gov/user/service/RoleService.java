package com.gov.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.user.dto.RoleDTO;
import com.gov.user.entity.RoleEntity;
import com.gov.user.vo.RoleVO;

import java.util.List;

public interface RoleService extends IService<RoleEntity> {

    List<RoleEntity> listAll();

    List<RoleVO> listAllVO();

    PageResult<RoleEntity> pageQuery(Long pageNum, Long pageSize, String roleName);

    PageResult<RoleVO> pageQueryVO(Long pageNum, Long pageSize, String roleName);

    RoleEntity getByRoleCode(String roleCode);

    RoleVO getVOByRoleCode(String roleCode);

    RoleVO getVOById(Long id);

    List<RoleEntity> listRolesByUserId(Long userId);

    List<RoleVO> listRolesVOByUserId(Long userId);

    void addRole(RoleDTO dto);

    void updateRole(RoleDTO dto);

    void deleteRole(Long id);
}
