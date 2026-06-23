package com.gov.monitor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.monitor.entity.LogEntity;
import com.gov.monitor.mapper.LogMapper;
import com.gov.monitor.service.LogService;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, LogEntity> implements LogService {
}
