package com.gov.datashare.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.datashare.dto.CatalogDTO;
import com.gov.datashare.entity.CatalogEntity;
import com.gov.datashare.vo.CatalogVO;

public interface CatalogService extends IService<CatalogEntity> {

    PageResult<CatalogVO> pageQueryVO(Long pageNum, Long pageSize, String keyword, String status);

    CatalogVO getVOById(Long id);

    void addCatalog(CatalogDTO dto);

    void updateCatalog(CatalogDTO dto);

    void deleteCatalog(Long id);
}
