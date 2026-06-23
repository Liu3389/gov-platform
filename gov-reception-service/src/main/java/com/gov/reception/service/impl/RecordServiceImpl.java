package com.gov.reception.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.reception.entity.RecordEntity;
import com.gov.reception.mapper.RecordMapper;
import com.gov.reception.service.RecordService;
import org.springframework.stereotype.Service;

@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, RecordEntity> implements RecordService {
}
