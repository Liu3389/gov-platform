package com.gov.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.user.dto.RegionDTO;
import com.gov.user.entity.RegionEntity;
import com.gov.user.vo.RegionVO;

import java.util.List;

public interface RegionService extends IService<RegionEntity> {

    List<RegionVO> listAllVO();

    List<RegionVO> listVOByParentCode(String parentCode);

    RegionVO getVOById(Long id);

    RegionVO getVOByCode(String regionCode);

    void addRegion(RegionDTO dto);

    void updateRegion(RegionDTO dto);

    void deleteRegion(Long id);
}
