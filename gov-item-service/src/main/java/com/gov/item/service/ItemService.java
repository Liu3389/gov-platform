package com.gov.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.item.dto.ItemDTO;
import com.gov.item.entity.ItemEntity;
import com.gov.item.vo.ItemVO;

public interface ItemService extends IService<ItemEntity> {

    PageResult<ItemVO> pageQueryVO(Long pageNum, Long pageSize, String itemName, Integer itemType);

    ItemVO getVOById(Long id);

    ItemVO getVOByItemCode(String itemCode);

    void addItem(ItemDTO dto);

    void updateItem(ItemDTO dto);

    void deleteItem(Long id);
}
