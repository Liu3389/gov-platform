package com.gov.open.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.open.dto.FeedbackDTO;
import com.gov.open.entity.FeedbackEntity;
import com.gov.open.mapper.FeedbackMapper;
import com.gov.open.service.FeedbackService;
import com.gov.open.vo.FeedbackVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 公开反馈Service实现
 */
@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, FeedbackEntity> implements FeedbackService {

    @Override
    public PageResult<FeedbackVO> pageQueryVO(Long pageNum, Long pageSize, Integer contentType, Long contentId, Integer status) {
        LambdaQueryWrapper<FeedbackEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FeedbackEntity::getDeleted, 0);
        wrapper.eq(contentType != null, FeedbackEntity::getContentType, String.valueOf(contentType));
        wrapper.eq(contentId != null, FeedbackEntity::getContentId, contentId);
        wrapper.eq(status != null, FeedbackEntity::getStatus, String.valueOf(status));
        wrapper.orderByDesc(FeedbackEntity::getCreateTime);

        Page<FeedbackEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<FeedbackVO> voList = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public FeedbackVO getVOById(Long id) {
        FeedbackEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            return null;
        }
        return convertToVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitFeedback(FeedbackDTO dto, Long userId) {
        FeedbackEntity entity = dto.toEntity();
        entity.setUserId(userId);
        entity.setStatus("0"); // 待处理
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replyFeedback(Long id, String replyContent, Long replyBy) {
        FeedbackEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "反馈不存在");
        }
        if (!"0".equals(entity.getStatus())) {
            throw new BusinessException(400, "该反馈不在待处理状态");
        }

        entity.setHandleResult(replyContent);
        entity.setHandleTime(LocalDateTime.now());
        entity.setStatus("1"); // 已处理

        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeFeedback(Long id) {
        FeedbackEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "反馈不存在");
        }

        entity.setStatus("2"); // 已关闭
        this.updateById(entity);
    }

    /**
     * Entity转VO
     */
    private FeedbackVO convertToVO(FeedbackEntity entity) {
        FeedbackVO vo = new FeedbackVO();
        BeanUtil.copyProperties(entity, vo);
        vo.setFeedbackTypeDesc(getFeedbackTypeDesc(entity.getFeedbackType()));
        vo.setStatusDesc(getStatusDesc(entity.getStatus()));
        return vo;
    }

    private String getFeedbackTypeDesc(String type) {
        if (type == null) return "未知";
        switch (type) {
            case "1": return "咨询";
            case "2": return "建议";
            case "3": return "投诉";
            default: return "未知";
        }
    }

    private String getStatusDesc(String status) {
        if (status == null) return "未知";
        switch (status) {
            case "0": return "待处理";
            case "1": return "已处理";
            case "2": return "已关闭";
            default: return "未知";
        }
    }
}
