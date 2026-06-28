package com.gov.message.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.message.dto.QueueDTO;
import com.gov.message.entity.QueueEntity;
import com.gov.message.mapper.QueueMapper;
import com.gov.message.service.QueueService;
import com.gov.message.vo.QueueVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QueueServiceImpl extends ServiceImpl<QueueMapper, QueueEntity> implements QueueService {

    @Override
    public PageResult<QueueVO> pageQueryVO(Long pageNum, Long pageSize, Integer queueStatus, Integer priority) {
        LambdaQueryWrapper<QueueEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(QueueEntity::getDeleted, 0);
        wrapper.eq(queueStatus != null, QueueEntity::getQueueStatus, queueStatus);
        wrapper.eq(priority != null, QueueEntity::getPriority, priority);
        wrapper.orderByDesc(QueueEntity::getPriority).orderByAsc(QueueEntity::getScheduledTime);
        Page<QueueEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<QueueVO> voList = page.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public QueueVO getVOById(Long id) {
        QueueEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "队列记录不存在");
        }
        return toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addQueue(QueueDTO dto) {
        QueueEntity entity = dto.toEntity();
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateQueue(QueueDTO dto) {
        QueueEntity entity = this.getById(dto.getId());
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "队列记录不存在");
        }
        BeanUtil.copyProperties(dto, entity, "id", "createTime", "createBy", "deleted");
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteQueue(Long id) {
        QueueEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "队列记录不存在");
        }
        this.removeById(id);
    }

    private QueueVO toVO(QueueEntity entity) {
        QueueVO vo = new QueueVO();
        BeanUtil.copyProperties(entity, vo);
        return vo;
    }
}
