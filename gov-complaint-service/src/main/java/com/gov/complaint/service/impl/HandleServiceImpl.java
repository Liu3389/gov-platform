package com.gov.complaint.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.complaint.dto.HandleDTO;
import com.gov.complaint.entity.HandleEntity;
import com.gov.complaint.mapper.HandleMapper;
import com.gov.complaint.service.HandleService;
import com.gov.complaint.utils.UserContext;
import com.gov.complaint.vo.HandleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HandleServiceImpl extends ServiceImpl<HandleMapper, HandleEntity> implements HandleService {

    @Override
    public PageResult<HandleVO> pageQueryVO(Long pageNum, Long pageSize, Long workId) {
        LambdaQueryWrapper<HandleEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HandleEntity::getDeleted, 0);
        wrapper.eq(workId != null, HandleEntity::getWorkId, workId);
        wrapper.orderByDesc(HandleEntity::getHandleTime);
        Page<HandleEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<HandleVO> voList = page.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public List<HandleVO> listByWorkId(Long workId) {
        List<HandleEntity> list = this.lambdaQuery()
                .eq(HandleEntity::getDeleted, 0)
                .eq(HandleEntity::getWorkId, workId)
                .orderByDesc(HandleEntity::getHandleTime)
                .list();
        return list.stream().map(this::toVO).collect(Collectors.toList());
    }

    @Override
    public HandleVO getVOById(Long id) {
        HandleEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "处理记录不存在");
        }
        return toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addHandle(HandleDTO dto) {
        HandleEntity entity = dto.toEntity();
        entity.setHandleTime(LocalDateTime.now());
        // 从网关Header自动填充当前处理人信息
        entity.setHandlerId(UserContext.getUserId());
        entity.setHandlerName(UserContext.getUsername());
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateHandle(HandleDTO dto) {
        HandleEntity entity = this.getById(dto.getId());
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "处理记录不存在");
        }
        BeanUtil.copyProperties(dto, entity, "id", "handleTime", "createTime", "createBy", "deleted");
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteHandle(Long id) {
        HandleEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "处理记录不存在");
        }
        this.removeById(id);
    }

    private HandleVO toVO(HandleEntity entity) {
        HandleVO vo = new HandleVO();
        BeanUtil.copyProperties(entity, vo);
        return vo;
    }
}
