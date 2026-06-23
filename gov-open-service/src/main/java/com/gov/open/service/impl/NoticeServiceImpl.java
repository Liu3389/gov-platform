package com.gov.open.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.open.entity.NoticeEntity;
import com.gov.open.mapper.NoticeMapper;
import com.gov.open.service.NoticeService;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, NoticeEntity> implements NoticeService {
}
