package com.gov.datashare.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.datashare.dto.LogDTO;
import com.gov.datashare.entity.LogEntity;
import com.gov.datashare.mapper.LogMapper;
import com.gov.datashare.service.LogService;
import com.gov.datashare.utils.UserContext;
import com.gov.datashare.vo.LogVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogServiceImpl extends ServiceImpl<LogMapper, LogEntity> implements LogService {

    @Override
    public PageResult<LogVO> pageQueryVO(Long pageNum, Long pageSize, Long apiId, String callResult) {
        LambdaQueryWrapper<LogEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LogEntity::getDeleted, 0);
        wrapper.eq(apiId != null, LogEntity::getApiId, apiId);
        wrapper.eq(callResult != null && !callResult.isEmpty(), LogEntity::getCallResult, callResult);
        wrapper.orderByDesc(LogEntity::getCreateTime);
        Page<LogEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<LogVO> voList = page.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public LogVO getVOById(Long id) {
        LogEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "日志记录不存在");
        }
        return toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addLog(LogDTO dto) {
        LogEntity entity = dto.toEntity();
        // 从网关Header自动填充当前用户信息
        entity.setCallerUserId(UserContext.getUserId());
        entity.setCallerDeptId(UserContext.getDeptId());
        entity.setCallTime(LocalDateTime.now());
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLog(LogDTO dto) {
        LogEntity entity = this.getById(dto.getId());
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "日志记录不存在");
        }
        BeanUtil.copyProperties(dto, entity, "id", "createTime", "createBy", "deleted");
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLog(Long id) {
        LogEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "日志记录不存在");
        }
        this.removeById(id);
    }

    private LogVO toVO(LogEntity entity) {
        LogVO vo = new LogVO();
        BeanUtil.copyProperties(entity, vo);
        return vo;
    }
}
