package com.gov.complaint.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.complaint.dto.HandleDTO;
import com.gov.complaint.entity.HandleEntity;
import com.gov.complaint.vo.HandleVO;

import java.util.List;

public interface HandleService extends IService<HandleEntity> {
    PageResult<HandleVO> pageQueryVO(Long pageNum, Long pageSize, Long workId);
    List<HandleVO> listByWorkId(Long workId);
    HandleVO getVOById(Long id);
    void addHandle(HandleDTO dto);
    void updateHandle(HandleDTO dto);
    void deleteHandle(Long id);
}
