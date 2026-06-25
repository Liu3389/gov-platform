package com.gov.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.item.convert.ItemConvert;
import com.gov.item.dto.ItemDTO;
import com.gov.item.entity.ItemEntity;
import com.gov.item.mapper.ItemMapper;
import com.gov.item.service.ItemService;
import com.gov.item.vo.ItemVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 事项服务实现
 */
@Service
@RequiredArgsConstructor
public class ItemServiceImpl extends ServiceImpl<ItemMapper, ItemEntity> implements ItemService {

    @Override
    public PageResult<ItemVO> pageQueryVO(Long pageNum, Long pageSize, String itemName, Integer itemType) {
        LambdaQueryWrapper<ItemEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ItemEntity::getDeleted, 0);
        if (StringUtils.hasText(itemName)) {
            wrapper.like(ItemEntity::getItemName, itemName);
        }
        if (itemType != null) {
            wrapper.eq(ItemEntity::getItemType, itemType);
        }
        wrapper.orderByDesc(ItemEntity::getCreateTime);
        Page<ItemEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<ItemVO> voList = ItemConvert.toVOList(page.getRecords());
        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public ItemVO getVOById(Long id) {
        ItemEntity entity = this.getById(id);
        if (entity == null) {
            throw BusinessException.notFound("事项不存在");
        }
        return ItemConvert.toVO(entity);
    }

    @Override
    public ItemVO getVOByItemCode(String itemCode) {
        ItemEntity entity = this.lambdaQuery()
                .eq(ItemEntity::getItemCode, itemCode)
                .eq(ItemEntity::getDeleted, 0)
                .one();
        if (entity == null) {
            throw BusinessException.notFound("事项不存在");
        }
        return ItemConvert.toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addItem(ItemDTO dto) {
        ItemEntity entity = ItemConvert.toEntity(dto);
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateItem(ItemDTO dto) {
        ItemEntity exist = this.getById(dto.getId());
        if (exist == null) {
            throw BusinessException.notFound("事项不存在");
        }
        ItemEntity entity = ItemConvert.toEntity(dto);
        entity.setId(dto.getId());
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteItem(Long id) {
        ItemEntity entity = this.getById(id);
        if (entity == null) {
            throw BusinessException.notFound("事项不存在");
        }
        this.removeById(id);
    }
}
