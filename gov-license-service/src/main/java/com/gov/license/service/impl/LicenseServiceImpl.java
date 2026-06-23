package com.gov.license.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.license.entity.LicenseEntity;
import com.gov.license.mapper.LicenseMapper;
import com.gov.license.service.LicenseService;
import org.springframework.stereotype.Service;

@Service
public class LicenseServiceImpl extends ServiceImpl<LicenseMapper, LicenseEntity> implements LicenseService {
}
