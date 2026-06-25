package com.gov.license.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.license.dto.*;
import com.gov.license.entity.LicenseEntity;
import com.gov.license.vo.LicenseDetailVO;
import com.gov.license.vo.LicenseVO;
import com.gov.common.result.PageResult;

public interface LicenseService extends IService<LicenseEntity> {

  PageResult<LicenseVO> pageQuery(Long pageNum, Long pageSize, String licenseNo, String keyword, String status);

  LicenseDetailVO getDetailById(Long id);

  LicenseVO generate(LicenseGenerateDTO dto);

  void sign(LicenseSignDTO dto, Long signUserId);

  LicenseDetailVO verify(LicenseVerifyDTO dto, String verifyIp);

  void auth(LicenseAuthDTO dto, Long authUserId);

  void updateLicense(Long id, LicenseGenerateDTO dto);

  void deleteLicense(Long id);
}
