package com.gov.datashare.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.datashare.entity.ApiEntity;
import com.gov.datashare.mapper.ApiMapper;
import com.gov.datashare.service.ApiService;
import org.springframework.stereotype.Service;

@Service
public class ApiServiceImpl extends ServiceImpl<ApiMapper, ApiEntity> implements ApiService {
}
