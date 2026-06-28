package com.gov.open.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.open.dto.ApplyDTO;
import com.gov.open.entity.ApplyEntity;
import com.gov.open.mapper.ApplyMapper;
import com.gov.open.service.ApplyService;
import com.gov.open.vo.ApplyVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 依申请公开Service实现
 */
@Service
@RequiredArgsConstructor
public class ApplyServiceImpl extends ServiceImpl<ApplyMapper, ApplyEntity> implements ApplyService {

    @Override
    public PageResult<ApplyVO> pageQueryVO(Long pageNum, Long pageSize, Integer status) {
        LambdaQueryWrapper<ApplyEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ApplyEntity::getDeleted, 0);
        wrapper.eq(status != null, ApplyEntity::getStatus, String.valueOf(status));
        wrapper.orderByDesc(ApplyEntity::getCreateTime);

        Page<ApplyEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<ApplyVO> voList = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public ApplyVO getVOById(Long id) {
        ApplyEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            return null;
        }
        return convertToVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApplyVO submitApply(ApplyDTO dto, Long userId, String userName, String userPhone, String userIdCard) {
        ApplyEntity entity = new ApplyEntity();
        BeanUtil.copyProperties(dto, entity);

        entity.setApplyNo(generateApplyNo());
        entity.setUserId(userId);
        entity.setUserName(userName != null ? userName : "匿名用户");
        entity.setUserPhone(userPhone);
        entity.setUserIdCard(userIdCard);
        entity.setApplyTime(LocalDateTime.now());
        entity.setStatus("0"); // 待处理
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());

        this.save(entity);
        return convertToVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void acceptApply(Long id) {
        ApplyEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "申请不存在");
        }
        if (!"0".equals(entity.getStatus())) {
            throw new BusinessException(400, "该申请不在待处理状态");
        }

        entity.setStatus("1"); // 已受理
        entity.setAcceptTime(LocalDateTime.now());
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replyApply(Long id, String replyContent, Long replyBy) {
        ApplyEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "申请不存在");
        }
        if (!"1".equals(entity.getStatus())) {
            throw new BusinessException(400, "该申请不在已受理状态");
        }

        entity.setReplyContent(replyContent);
        entity.setReplyTime(LocalDateTime.now());
        entity.setStatus("2"); // 已答复

        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectApply(Long id, String rejectReason) {
        ApplyEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "申请不存在");
        }

        entity.setRejectReason(rejectReason);
        entity.setStatus("3"); // 不予公开

        this.updateById(entity);
    }

    /**
     * 生成申请编号
     */
    private String generateApplyNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "OGA" + dateStr;
    }

    /**
     * Entity转VO
     */
    private ApplyVO convertToVO(ApplyEntity entity) {
        ApplyVO vo = new ApplyVO();
        BeanUtil.copyProperties(entity, vo);
        vo.setApplyTypeDesc(getApplyTypeDesc(entity.getApplyType()));
        vo.setStatusDesc(getStatusDesc(entity.getStatus()));
        return vo;
    }

    private String getApplyTypeDesc(String type) {
        if (type == null) return "未知";
        switch (type) {
            case "1": return "网上";
            case "2": return "现场";
            case "3": return "信函";
            case "4": return "传真";
            default: return "未知";
        }
    }

    private String getStatusDesc(String status) {
        if (status == null) return "未知";
        switch (status) {
            case "0": return "待处理";
            case "1": return "已受理";
            case "2": return "已答复";
            case "3": return "不予公开";
            default: return "未知";
        }
    }
}
