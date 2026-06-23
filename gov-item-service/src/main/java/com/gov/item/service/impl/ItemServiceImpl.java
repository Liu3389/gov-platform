package com.gov.item.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.item.entity.ItemEntity;
import com.gov.item.mapper.ItemMapper;
import com.gov.item.service.ItemService;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, ItemEntity> implements ItemService {
}
