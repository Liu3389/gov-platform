package com.gov.datashare.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.datashare.dto.SubscribeDTO;
import com.gov.datashare.entity.SubscribeEntity;
import com.gov.datashare.mapper.SubscribeMapper;
import com.gov.datashare.service.SubscribeService;
import com.gov.datashare.utils.UserContext;
import com.gov.datashare.vo.SubscribeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscribeServiceImpl extends ServiceImpl<SubscribeMapper, SubscribeEntity> implements SubscribeService {

    @Override
    public PageResult<SubscribeVO> pageQueryVO(Long pageNum, Long pageSize, Long apiId, String status) {
        LambdaQueryWrapper<SubscribeEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SubscribeEntity::getDeleted, 0);
        wrapper.eq(apiId != null, SubscribeEntity::getApiId, apiId);
        wrapper.eq(StrUtil.isNotBlank(status), SubscribeEntity::getStatus, status);
        wrapper.orderByDesc(SubscribeEntity::getCreateTime);
        Page<SubscribeEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<SubscribeVO> voList = page.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public SubscribeVO getVOById(Long id) {
        SubscribeEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "订阅记录不存在");
        }
        return toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSubscribe(SubscribeDTO dto) {
        SubscribeEntity entity = dto.toEntity();
        // 从网关Header自动填充当前用户信息
        entity.setSubscribeUserId(UserContext.getUserId());
        entity.setSubscribeUserName(UserContext.getUsername());
        entity.setSubscribeTime(LocalDateTime.now());
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSubscribe(SubscribeDTO dto) {
        SubscribeEntity entity = this.getById(dto.getId());
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "订阅记录不存在");
        }
        BeanUtil.copyProperties(dto, entity, "id", "createTime", "createBy", "deleted");
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSubscribe(Long id) {
        SubscribeEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "订阅记录不存在");
        }
        this.removeById(id);
    }

    private SubscribeVO toVO(SubscribeEntity entity) {
        SubscribeVO vo = new SubscribeVO();
        BeanUtil.copyProperties(entity, vo);
        return vo;
    }
}
