package com.gov.open.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.open.dto.NoticeDTO;
import com.gov.open.dto.NoticeQueryDTO;
import com.gov.open.entity.NoticeEntity;
import com.gov.open.mapper.NoticeMapper;
import com.gov.open.service.NoticeService;
import com.gov.open.vo.NoticeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通知公告Service实现
 */
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, NoticeEntity> implements NoticeService {

    @Override
    public PageResult<NoticeVO> pageQueryVO(Long pageNum, Long pageSize, NoticeQueryDTO queryDTO) {
        LambdaQueryWrapper<NoticeEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NoticeEntity::getDeleted, 0);
        wrapper.like(queryDTO.getKeyword() != null, NoticeEntity::getTitle, queryDTO.getKeyword());
        wrapper.eq(queryDTO.getStatus() != null, NoticeEntity::getStatus, String.valueOf(queryDTO.getStatus()));
        wrapper.orderByDesc(NoticeEntity::getTopFlag);
        wrapper.orderByDesc(NoticeEntity::getPublishTime);

        Page<NoticeEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<NoticeVO> voList = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public NoticeVO getVOById(Long id) {
        NoticeEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            return null;
        }
        return convertToVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addNotice(NoticeDTO dto, Long publishDeptId) {
        NoticeEntity entity = new NoticeEntity();
        BeanUtil.copyProperties(dto, entity);

        entity.setNoticeCode(generateNoticeCode());
        entity.setPublishDeptId(publishDeptId);
        entity.setPublishUserId(1L); // 默认用户ID
        entity.setPublishTime(LocalDateTime.now()); // DB NOT NULL约束，发布时更新为实际发布时间
        entity.setViewCount(0);
        if (entity.getTopFlag() == null) {
            entity.setTopFlag(0);
        }
        if (entity.getStatus() == null) {
            entity.setStatus("0"); // 默认草稿
        }
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());

        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNotice(NoticeDTO dto, Long id) {
        NoticeEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "公告不存在");
        }

        BeanUtil.copyProperties(dto, entity, "id", "noticeCode", "publishDeptId", "viewCount", "createTime", "createBy");
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publishNotice(Long id) {
        NoticeEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "公告不存在");
        }

        entity.setStatus("1"); // 已发布
        entity.setPublishTime(LocalDateTime.now());
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void withdrawNotice(Long id) {
        NoticeEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "公告不存在");
        }

        entity.setStatus("2"); // 已撤回
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void incrementViewCount(Long id) {
        NoticeEntity entity = this.getById(id);
        if (entity != null) {
            entity.setViewCount(entity.getViewCount() + 1);
            this.updateById(entity);
        }
    }

    /**
     * 生成公告编号
     */
    private String generateNoticeCode() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return "NOT" + dateStr;
    }

    /**
     * Entity转VO
     */
    private NoticeVO convertToVO(NoticeEntity entity) {
        NoticeVO vo = new NoticeVO();
        BeanUtil.copyProperties(entity, vo);
        vo.setStatusDesc(getStatusDesc(entity.getStatus()));
        return vo;
    }

    private String getStatusDesc(String status) {
        if (status == null) return "未知";
        switch (status) {
            case "0": return "草稿";
            case "1": return "已发布";
            case "2": return "已撤回";
            default: return "未知";
        }
    }
}
