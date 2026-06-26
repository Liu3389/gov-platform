package com.gov.reception.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.common.utils.RedisUtil;
import com.gov.reception.entity.QueueEntity;
import com.gov.reception.mapper.QueueMapper;
import com.gov.reception.service.QueueService;
import com.gov.reception.vo.QueueVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 排队叫号Service实现
 */
@Service
@RequiredArgsConstructor
public class QueueServiceImpl extends ServiceImpl<QueueMapper, QueueEntity> implements QueueService {

    private final RedisUtil redisUtil;

    @Override
    public PageResult<QueueVO> pageQueryVO(Long pageNum, Long pageSize, Long windowId, Integer status) {
        LambdaQueryWrapper<QueueEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(windowId != null, QueueEntity::getWindowId, windowId);
        wrapper.eq(status != null, QueueEntity::getStatus, String.valueOf(status));
        wrapper.orderByDesc(QueueEntity::getCreateTime);

        Page<QueueEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<QueueVO> voList = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QueueVO takeQueue(Long userId, Long itemId) {
        // 生成排队号码
        String queueNo = generateQueueNo();

        QueueEntity entity = new QueueEntity();
        entity.setQueueNo(queueNo);
        entity.setUserId(userId);
        entity.setItemId(itemId);
        entity.setQueueTime(LocalDateTime.now());
        entity.setStatus("0"); // 等待中
        entity.setPriority(0);
        this.save(entity);

        return convertToVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void callQueue(Long id, Long windowId) {
        QueueEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "排队记录不存在");
        }
        if (!"0".equals(entity.getStatus())) {
            throw new BusinessException(400, "该排队记录不在等待状态");
        }

        entity.setWindowId(windowId);
        entity.setCallTime(LocalDateTime.now());
        entity.setStatus("1"); // 办理中
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finishQueue(Long id) {
        QueueEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "排队记录不存在");
        }
        if (!"1".equals(entity.getStatus())) {
            throw new BusinessException(400, "该排队记录不在办理状态");
        }

        entity.setFinishTime(LocalDateTime.now());
        entity.setStatus("2"); // 已完成
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelQueue(Long id) {
        QueueEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "排队记录不存在");
        }
        if (!"0".equals(entity.getStatus())) {
            throw new BusinessException(400, "只能取消等待中的排队");
        }

        entity.setStatus("3"); // 已取消
        this.updateById(entity);
    }

    @Override
    public int getWaitCount(Long id) {
        QueueEntity entity = this.getById(id);
        if (entity == null) {
            return 0;
        }

        LambdaQueryWrapper<QueueEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(QueueEntity::getStatus, "0");
        wrapper.lt(QueueEntity::getQueueTime, entity.getQueueTime());
        return (int) this.count(wrapper);
    }

    /**
     * 生成排队号码
     */
    private String generateQueueNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String redisKey = "gov:queue:seq:" + dateStr;
        Long seq = redisUtil.incrBy(redisKey, 1);
        return "Q" + dateStr + String.format("%04d", seq);
    }

    /**
     * Entity转VO
     */
    private QueueVO convertToVO(QueueEntity entity) {
        QueueVO vo = new QueueVO();
        BeanUtil.copyProperties(entity, vo);
        vo.setStatusDesc(getStatusDesc(entity.getStatus()));
        vo.setWaitCount(getWaitCount(entity.getId()));
        return vo;
    }

    /**
     * 获取状态描述
     */
    private String getStatusDesc(String status) {
        if (status == null) return "未知";
        switch (status) {
            case "0": return "等待中";
            case "1": return "办理中";
            case "2": return "已完成";
            case "3": return "已取消";
            default: return "未知";
        }
    }
}
