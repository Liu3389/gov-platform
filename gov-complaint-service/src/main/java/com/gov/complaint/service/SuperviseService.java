package com.gov.complaint.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.complaint.dto.SuperviseDTO;
import com.gov.complaint.entity.SuperviseEntity;
import com.gov.complaint.vo.SuperviseVO;

import java.util.List;

public interface SuperviseService extends IService<SuperviseEntity> {
    PageResult<SuperviseVO> pageQueryVO(Long pageNum, Long pageSize, Long workId, String status);
    List<SuperviseVO> listByWorkId(Long workId);
    SuperviseVO getVOById(Long id);
    void addSupervise(SuperviseDTO dto);
    void updateSupervise(SuperviseDTO dto);
    void deleteSupervise(Long id);
}
