package com.gov.message.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.message.dto.RecordDTO;
import com.gov.message.entity.RecordEntity;
import com.gov.message.mapper.RecordMapper;
import com.gov.message.service.RecordService;
import com.gov.message.utils.UserContext;
import com.gov.message.vo.RecordVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl extends ServiceImpl<RecordMapper, RecordEntity> implements RecordService {

    @Override
    public PageResult<RecordVO> pageQueryVO(Long pageNum, Long pageSize, String channel, String sendStatus) {
        LambdaQueryWrapper<RecordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RecordEntity::getDeleted, 0);
        wrapper.eq(StrUtil.isNotBlank(channel), RecordEntity::getChannel, channel);
        wrapper.eq(StrUtil.isNotBlank(sendStatus), RecordEntity::getSendStatus, sendStatus);
        wrapper.orderByDesc(RecordEntity::getCreateTime);
        Page<RecordEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<RecordVO> voList = page.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public RecordVO getVOById(Long id) {
        RecordEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "记录不存在");
        }
        return toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRecord(RecordDTO dto) {
        RecordEntity entity = dto.toEntity();
        // 从网关Header自动填充当前用户信息
        entity.setReceiverId(UserContext.getUserId());
        entity.setReceiverName(UserContext.getUsername());
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRecord(RecordDTO dto) {
        RecordEntity entity = this.getById(dto.getId());
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "记录不存在");
        }
        BeanUtil.copyProperties(dto, entity, "id", "createTime", "createBy", "deleted");
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRecord(Long id) {
        RecordEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "记录不存在");
        }
        this.removeById(id);
    }

    private RecordVO toVO(RecordEntity entity) {
        RecordVO vo = new RecordVO();
        BeanUtil.copyProperties(entity, vo);
        // 敏感字段脱敏
        if (vo.getReceiverPhone() != null && vo.getReceiverPhone().length() > 7) {
            vo.setReceiverPhone(vo.getReceiverPhone().substring(0, 3) + "****" + vo.getReceiverPhone().substring(7));
        }
        return vo;
    }
}
