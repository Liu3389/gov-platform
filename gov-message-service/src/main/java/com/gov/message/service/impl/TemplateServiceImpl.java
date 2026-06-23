package com.gov.message.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.message.entity.TemplateEntity;
import com.gov.message.mapper.TemplateMapper;
import com.gov.message.service.TemplateService;
import org.springframework.stereotype.Service;

@Service
public class TemplateServiceImpl extends ServiceImpl<TemplateMapper, TemplateEntity> implements TemplateService {
}
