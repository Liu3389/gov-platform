package com.gov.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.user.dto.UserRealnameDTO;
import com.gov.user.entity.UserRealnameEntity;
import com.gov.user.mapper.UserRealnameMapper;
import com.gov.user.service.UserRealnameService;
import com.gov.user.vo.UserRealnameVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserRealnameServiceImpl extends ServiceImpl<UserRealnameMapper, UserRealnameEntity> implements UserRealnameService {

    @Override
    public UserRealnameVO getVOByUserId(Long userId) {
        UserRealnameEntity entity = this.lambdaQuery()
                .eq(UserRealnameEntity::getDeleted, 0)
                .eq(UserRealnameEntity::getUserId, userId)
                .orderByDesc(UserRealnameEntity::getCreateTime)
                .last("LIMIT 1")
                .one();
        if (entity == null) {
            throw BusinessException.notFound("实名认证记录不存在");
        }
        return toVO(entity);
    }

    @Override
    public PageResult<UserRealnameVO> pageQueryVO(Long pageNum, Long pageSize, Integer verifyStatus) {
        LambdaQueryWrapper<UserRealnameEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRealnameEntity::getDeleted, 0);
        if (verifyStatus != null) {
            wrapper.eq(UserRealnameEntity::getVerifyStatus, verifyStatus);
        }
        wrapper.orderByDesc(UserRealnameEntity::getCreateTime);
        var page = this.page(new Page<>(pageNum, pageSize), wrapper);
        return PageResult.of(page.getRecords().stream().map(this::toVO).toList(), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitRealname(UserRealnameDTO dto) {
        // 检查是否已有待审核的记录
        UserRealnameEntity existing = this.lambdaQuery()
                .eq(UserRealnameEntity::getDeleted, 0)
                .eq(UserRealnameEntity::getUserId, dto.getUserId())
                .eq(UserRealnameEntity::getVerifyStatus, 0)
                .one();
        if (existing != null) {
            throw new BusinessException(400, "已有待审核的实名认证申请");
        }
        UserRealnameEntity entity = new UserRealnameEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setVerifyStatus(0);
        entity.setCreateTime(LocalDateTime.now());
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void verifyRealname(Long id, Integer verifyStatus, String verifyRemark) {
        UserRealnameEntity entity = this.getById(id);
        if (entity == null) {
            throw BusinessException.notFound("实名认证记录不存在");
        }
        if (entity.getVerifyStatus() != 0) {
            throw new BusinessException(400, "该记录已审核");
        }
        entity.setVerifyStatus(verifyStatus);
        entity.setVerifyTime(LocalDateTime.now());
        entity.setVerifyRemark(verifyRemark);
        this.updateById(entity);
    }

    private UserRealnameVO toVO(UserRealnameEntity entity) {
        UserRealnameVO vo = new UserRealnameVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}
