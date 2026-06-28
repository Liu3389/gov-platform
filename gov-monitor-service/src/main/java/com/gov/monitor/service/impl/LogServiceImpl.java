package com.gov.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.event.OperateLogEvent;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.monitor.convert.LogConvert;
import com.gov.monitor.dto.LogDTO;
import com.gov.monitor.entity.LogEntity;
import com.gov.monitor.mapper.LogMapper;
import com.gov.monitor.service.LogService;
import com.gov.monitor.vo.LogVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogServiceImpl extends ServiceImpl<LogMapper, LogEntity> implements LogService {

    @Override
    public PageResult<LogVO> pageQueryVO(Long pageNum, Long pageSize) {
        LambdaQueryWrapper<LogEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LogEntity::getDeleted, 0);
        wrapper.orderByDesc(LogEntity::getOperateTime);
        Page<LogEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<LogVO> voList = LogConvert.toVOList(page.getRecords());
        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public LogVO getVOById(Long id) {
        LogEntity entity = this.getById(id);
        if (entity == null) {
            throw BusinessException.notFound("日志不存在");
        }
        return LogConvert.toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addLog(LogDTO dto) {
        LogEntity entity = LogConvert.toEntity(dto);
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLog(Long id) {
        LogEntity entity = this.getById(id);
        if (entity == null) {
            throw BusinessException.notFound("日志不存在");
        }
        this.removeById(id);
    }

    @Override
    public PageResult<LogVO> pageAuditVO(Long pageNum, Long pageSize, LocalDateTime operateStart, LocalDateTime operateEnd) {
        LambdaQueryWrapper<LogEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LogEntity::getDeleted, 0);
        if (operateStart != null) {
            wrapper.ge(LogEntity::getOperateTime, operateStart);
        }
        if (operateEnd != null) {
            wrapper.le(LogEntity::getOperateTime, operateEnd);
        }
        wrapper.orderByDesc(LogEntity::getOperateTime);
        Page<LogEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<LogVO> voList = LogConvert.toVOList(page.getRecords());
        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordLog(OperateLogEvent event) {
        LogEntity entity = new LogEntity();
        entity.setUserId(event.getUserId());
        entity.setUserName(event.getUserName());
        entity.setDeptId(event.getDeptId());
        entity.setDeptName(event.getDeptName());
        entity.setModule(event.getModule());
        entity.setAction(event.getAction());
        entity.setMethod(event.getMethod());
        entity.setRequestUrl(event.getRequestUrl());
        entity.setRequestType(event.getRequestType());
        entity.setRequestParams(event.getRequestParams());
        entity.setResponseData(event.getResponseData());
        entity.setOperateTime(event.getOperateTime());
        entity.setOperateIp(event.getOperateIp());
        entity.setExecuteTime(event.getExecuteTime() != null ? event.getExecuteTime().intValue() : null);
        entity.setStatus(event.getStatus());
        entity.setErrorMsg(event.getErrorMsg());
        this.save(entity);
    }
}
