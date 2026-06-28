package com.gov.datashare.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.datashare.dto.PermissionDTO;
import com.gov.datashare.entity.PermissionEntity;
import com.gov.datashare.vo.PermissionVO;

public interface PermissionService extends IService<PermissionEntity> {

    PageResult<PermissionVO> pageQueryVO(Long pageNum, Long pageSize, Long apiId, String status);

    PermissionVO getVOById(Long id);

    void addPermission(PermissionDTO dto);

    void updatePermission(PermissionDTO dto);

    void deletePermission(Long id);
}
