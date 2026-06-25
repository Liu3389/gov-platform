package com.gov.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.user.dto.DeptDTO;
import com.gov.user.entity.DeptEntity;
import com.gov.user.vo.DeptVO;

import java.util.List;

public interface DeptService extends IService<DeptEntity> {

    List<DeptEntity> listAll();

    List<DeptVO> listAllVO();

    PageResult<DeptEntity> pageQuery(Long pageNum, Long pageSize, String deptName);

    PageResult<DeptVO> pageQueryVO(Long pageNum, Long pageSize, String deptName);

    DeptEntity getByDeptCode(String deptCode);

    DeptVO getVOByDeptCode(String deptCode);

    DeptVO getVOById(Long id);

    void addDept(DeptDTO dto);

    void updateDept(DeptDTO dto);

    void deleteDept(Long id);
}
