package com.gov.complaint.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.complaint.dto.WorkDTO;
import com.gov.complaint.entity.WorkEntity;
import com.gov.complaint.vo.WorkVO;

public interface WorkService extends IService<WorkEntity> {

    PageResult<WorkVO> pageQueryVO(Long pageNum, Long pageSize, String keyword, String status);

    WorkVO getVOById(Long id);

    void addWork(WorkDTO dto);

    void updateWork(WorkDTO dto);

    void deleteWork(Long id);
}
