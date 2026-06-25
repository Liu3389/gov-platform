package com.gov.complaint.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.complaint.dto.WorkDTO;
import com.gov.complaint.entity.WorkEntity;
import com.gov.complaint.mapper.WorkMapper;
import com.gov.complaint.service.WorkService;
import com.gov.complaint.utils.UserContext;
import com.gov.complaint.vo.WorkVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkServiceImpl extends ServiceImpl<WorkMapper, WorkEntity> implements WorkService {

    @Override
    public PageResult<WorkVO> pageQueryVO(Long pageNum, Long pageSize, String keyword, String status) {
        LambdaQueryWrapper<WorkEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WorkEntity::getDeleted, 0);
        wrapper.like(StrUtil.isNotBlank(keyword), WorkEntity::getTitle, keyword);
        wrapper.eq(StrUtil.isNotBlank(status), WorkEntity::getStatus, status);
        wrapper.orderByDesc(WorkEntity::getCreateTime);
        Page<WorkEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<WorkVO> voList = page.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public WorkVO getVOById(Long id) {
        WorkEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "工单不存在");
        }
        return toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addWork(WorkDTO dto) {
        WorkEntity entity = dto.toEntity();
        entity.setWorkNo(generateWorkNo());
        entity.setStatus("0");
        entity.setSubmitTime(LocalDateTime.now());
        // 从网关Header自动填充当前用户信息
        entity.setUserId(UserContext.getUserId());
        entity.setUserName(UserContext.getUsername());
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWork(WorkDTO dto) {
        WorkEntity entity = this.getById(dto.getId());
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "工单不存在");
        }
        BeanUtil.copyProperties(dto, entity, "id", "workNo", "submitTime", "createTime", "createBy", "deleted");
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteWork(Long id) {
        WorkEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "工单不存在");
        }
        this.removeById(id);
    }

    /**
     * Entity → VO 转换
     */
    private WorkVO toVO(WorkEntity entity) {
        WorkVO vo = new WorkVO();
        BeanUtil.copyProperties(entity, vo);
        return vo;
    }

    /**
     * 生成工单编号：WO + yyyyMMddHHmmss + 4位随机数
     */
    private String generateWorkNo() {
        String dateStr = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int random = (int) (Math.random() * 9000) + 1000;
        return "WO" + dateStr + random;
    }
}
