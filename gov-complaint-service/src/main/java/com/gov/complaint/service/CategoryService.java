package com.gov.complaint.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.complaint.dto.CategoryDTO;
import com.gov.complaint.entity.CategoryEntity;
import com.gov.complaint.vo.CategoryVO;

public interface CategoryService extends IService<CategoryEntity> {
    PageResult<CategoryVO> pageQueryVO(Long pageNum, Long pageSize, String keyword, String status);
    CategoryVO getVOById(Long id);
    void addCategory(CategoryDTO dto);
    void updateCategory(CategoryDTO dto);
    void deleteCategory(Long id);
}
