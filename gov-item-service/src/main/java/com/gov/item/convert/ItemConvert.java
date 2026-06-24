package com.gov.item.convert;

import com.gov.item.dto.ItemDTO;
import com.gov.item.entity.ItemEntity;
import com.gov.item.vo.ItemVO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemConvert {

    public static ItemEntity toEntity(ItemDTO dto) {
        if (dto == null) return null;
        ItemEntity entity = new ItemEntity();
        entity.setId(dto.getId());
        entity.setItemCode(dto.getItemCode());
        entity.setItemName(dto.getItemName());
        entity.setDeptId(dto.getDeptId());
        entity.setItemType(dto.getItemType());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    public static ItemVO toVO(ItemEntity entity) {
        if (entity == null) return null;
        ItemVO vo = new ItemVO();
        vo.setId(entity.getId());
        vo.setItemCode(entity.getItemCode());
        vo.setItemName(entity.getItemName());
        vo.setDeptId(entity.getDeptId());
        vo.setItemType(entity.getItemType());
        vo.setStatus(entity.getStatus());
        vo.setCreateTime(entity.getCreateTime());
        vo.setUpdateTime(entity.getUpdateTime());
        return vo;
    }

    public static List<ItemVO> toVOList(List<ItemEntity> entities) {
        if (entities == null) return null;
        return entities.stream()
                .map(ItemConvert::toVO)
                .collect(Collectors.toList());
    }
}
