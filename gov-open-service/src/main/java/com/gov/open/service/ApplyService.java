package com.gov.open.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.open.dto.ApplyDTO;
import com.gov.open.entity.ApplyEntity;
import com.gov.open.vo.ApplyVO;

/**
 * 依申请公开Service
 */
public interface ApplyService extends IService<ApplyEntity> {

    /**
     * 分页查询依申请公开
     */
    PageResult<ApplyVO> pageQueryVO(Long pageNum, Long pageSize, Integer status);

    /**
     * 根据ID查询VO
     */
    ApplyVO getVOById(Long id);

    /**
     * 提交申请
     */
    ApplyVO submitApply(ApplyDTO dto, Long userId, String userName, String userPhone, String userIdCard);

    /**
     * 受理申请
     */
    void acceptApply(Long id);

    /**
     * 答复申请
     */
    void replyApply(Long id, String replyContent, Long replyBy);

    /**
     * 不予公开
     */
    void rejectApply(Long id, String rejectReason);
}
