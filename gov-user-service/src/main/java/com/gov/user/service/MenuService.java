package com.gov.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.user.dto.MenuDTO;
import com.gov.user.entity.MenuEntity;
import com.gov.user.vo.MenuVO;

import java.util.List;

public interface MenuService extends IService<MenuEntity> {

    List<MenuEntity> listAll();

    List<MenuVO> listAllVO();

    List<MenuEntity> listMenusByUserId(Long userId);

    List<MenuVO> listMenusVOByUserId(Long userId);

    List<MenuEntity> listMenusByRoleId(Long roleId);

    List<MenuVO> listMenusVOByRoleId(Long roleId);

    MenuVO getVOById(Long id);

    void addMenu(MenuDTO dto);

    void updateMenu(MenuDTO dto);

    void deleteMenu(Long id);
}
