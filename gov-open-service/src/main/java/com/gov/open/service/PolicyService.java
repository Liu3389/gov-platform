package com.gov.open.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.open.dto.PolicyDTO;
import com.gov.open.entity.PolicyEntity;
import com.gov.open.vo.PolicyVO;

/**
 * 政策法规Service
 */
public interface PolicyService extends IService<PolicyEntity> {

    /**
     * 分页查询政策法规
     */
    PageResult<PolicyVO> pageQueryVO(Long pageNum, Long pageSize, Integer policyType, Integer status);

    /**
     * 根据ID查询VO
     */
    PolicyVO getVOById(Long id);

    /**
     * 新增政策法规
     */
    void addPolicy(PolicyDTO dto, Long publishDeptId);

    /**
     * 更新政策法规
     */
    void updatePolicy(PolicyDTO dto, Long id);

    /**
     * 发布政策法规
     */
    void publishPolicy(Long id);

    /**
     * 废止政策法规
     */
    void abolishPolicy(Long id);
}
