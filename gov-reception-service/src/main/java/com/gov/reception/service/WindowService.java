package com.gov.reception.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.reception.dto.WindowDTO;
import com.gov.reception.entity.WindowEntity;
import com.gov.reception.vo.WindowVO;

/**
 * 窗口Service
 */
public interface WindowService extends IService<WindowEntity> {

    /**
     * 分页查询窗口
     */
    PageResult<WindowVO> pageQueryVO(Long pageNum, Long pageSize, Long deptId, String status);

    /**
     * 根据ID查询窗口VO
     */
    WindowVO getVOById(Long id);

    /**
     * 新增窗口
     */
    void addWindow(WindowDTO dto);

    /**
     * 更新窗口
     */
    void updateWindow(WindowDTO dto, Long id);
}
