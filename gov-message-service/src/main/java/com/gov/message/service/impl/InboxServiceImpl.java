package com.gov.message.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.message.dto.InboxDTO;
import com.gov.message.entity.InboxEntity;
import com.gov.message.mapper.InboxMapper;
import com.gov.message.service.InboxService;
import com.gov.message.utils.UserContext;
import com.gov.message.vo.InboxVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InboxServiceImpl extends ServiceImpl<InboxMapper, InboxEntity> implements InboxService {

    @Override
    public PageResult<InboxVO> pageQueryVO(Long pageNum, Long pageSize, Long userId, Integer isRead) {
        LambdaQueryWrapper<InboxEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InboxEntity::getDeleted, 0);
        wrapper.eq(userId != null, InboxEntity::getUserId, userId);
        wrapper.eq(isRead != null, InboxEntity::getIsRead, isRead);
        wrapper.orderByDesc(InboxEntity::getCreateTime);
        Page<InboxEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<InboxVO> voList = page.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public InboxVO getVOById(Long id) {
        InboxEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "站内信不存在");
        }
        return toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addInbox(InboxDTO dto) {
        InboxEntity entity = dto.toEntity();
        // 从网关Header自动填充当前用户信息
        entity.setUserId(UserContext.getUserId());
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInbox(InboxDTO dto) {
        InboxEntity entity = this.getById(dto.getId());
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "站内信不存在");
        }
        BeanUtil.copyProperties(dto, entity, "id", "createTime", "createBy", "deleted");
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteInbox(Long id) {
        InboxEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "站内信不存在");
        }
        this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAsRead(Long id) {
        InboxEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "站内信不存在");
        }
        entity.setIsRead(1);
        entity.setReadTime(LocalDateTime.now());
        this.updateById(entity);
    }

    private InboxVO toVO(InboxEntity entity) {
        InboxVO vo = new InboxVO();
        BeanUtil.copyProperties(entity, vo);
        return vo;
    }
}
