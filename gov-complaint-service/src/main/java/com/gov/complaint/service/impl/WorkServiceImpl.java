package com.gov.complaint.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.complaint.entity.WorkEntity;
import com.gov.complaint.mapper.WorkMapper;
import com.gov.complaint.service.WorkService;
import org.springframework.stereotype.Service;

@Service
public class WorkServiceImpl extends ServiceImpl<WorkMapper, WorkEntity> implements WorkService {
}
