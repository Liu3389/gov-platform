package com.gov.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.user.dto.LoginLogDTO;
import com.gov.user.entity.LoginLogEntity;
import com.gov.user.mapper.LoginLogMapper;
import com.gov.user.service.LoginLogService;
import com.gov.user.vo.LoginLogVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLogEntity> implements LoginLogService {

    @Override
    public PageResult<LoginLogVO> pageQueryVO(Long pageNum, Long pageSize, Long userId) {
        LambdaQueryWrapper<LoginLogEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LoginLogEntity::getDeleted, 0);
        if (userId != null) {
            wrapper.eq(LoginLogEntity::getUserId, userId);
        }
        wrapper.orderByDesc(LoginLogEntity::getLoginTime);
        var page = this.page(new Page<>(pageNum, pageSize), wrapper);
        return PageResult.of(page.getRecords().stream().map(this::toVO).toList(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public LoginLogVO getVOById(Long id) {
        LoginLogEntity entity = this.getById(id);
        if (entity == null) {
            throw BusinessException.notFound("日志不存在");
        }
        return toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordLogin(LoginLogDTO dto) {
        LoginLogEntity entity = new LoginLogEntity();
        BeanUtils.copyProperties(dto, entity);
        if (entity.getLoginTime() == null) {
            entity.setLoginTime(LocalDateTime.now());
        }
        if (entity.getCreateTime() == null) {
            entity.setCreateTime(LocalDateTime.now());
        }
        this.save(entity);
    }

    private LoginLogVO toVO(LoginLogEntity entity) {
        LoginLogVO vo = new LoginLogVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}
