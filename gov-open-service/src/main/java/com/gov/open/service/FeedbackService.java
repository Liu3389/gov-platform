package com.gov.open.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.open.dto.FeedbackDTO;
import com.gov.open.entity.FeedbackEntity;
import com.gov.open.vo.FeedbackVO;

/**
 * 公开反馈Service
 */
public interface FeedbackService extends IService<FeedbackEntity> {

    /**
     * 分页查询反馈
     */
    PageResult<FeedbackVO> pageQueryVO(Long pageNum, Long pageSize, Integer contentType, Long contentId, Integer status);

    /**
     * 根据ID查询VO
     */
    FeedbackVO getVOById(Long id);

    /**
     * 提交反馈
     */
    void submitFeedback(FeedbackDTO dto, Long userId);

    /**
     * 回复反馈
     */
    void replyFeedback(Long id, String replyContent, Long replyBy);

    /**
     * 关闭反馈
     */
    void closeFeedback(Long id);
}
