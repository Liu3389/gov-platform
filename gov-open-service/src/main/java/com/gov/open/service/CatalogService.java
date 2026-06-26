package com.gov.open.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.open.dto.CatalogDTO;
import com.gov.open.entity.CatalogEntity;
import com.gov.open.vo.CatalogVO;

import java.util.List;

/**
 * 公开目录Service
 */
public interface CatalogService extends IService<CatalogEntity> {

    /**
     * 查询目录树
     */
    List<CatalogVO> getCatalogTree(Integer catalogType);

    /**
     * 根据ID查询VO
     */
    CatalogVO getVOById(Long id);

    /**
     * 新增目录
     */
    void addCatalog(CatalogDTO dto);

    /**
     * 更新目录
     */
    void updateCatalog(CatalogDTO dto, Long id);

    /**
     * 删除目录
     */
    void deleteCatalog(Long id);
}
