package com.gov.complaint.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.complaint.dto.SuperviseDTO;
import com.gov.complaint.entity.SuperviseEntity;
import com.gov.complaint.mapper.SuperviseMapper;
import com.gov.complaint.service.SuperviseService;
import com.gov.complaint.utils.UserContext;
import com.gov.complaint.vo.SuperviseVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SuperviseServiceImpl extends ServiceImpl<SuperviseMapper, SuperviseEntity> implements SuperviseService {

    @Override
    public PageResult<SuperviseVO> pageQueryVO(Long pageNum, Long pageSize, Long workId, String status) {
        LambdaQueryWrapper<SuperviseEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SuperviseEntity::getDeleted, 0);
        wrapper.eq(workId != null, SuperviseEntity::getWorkId, workId);
        wrapper.eq(StrUtil.isNotBlank(status), SuperviseEntity::getStatus, status);
        wrapper.orderByDesc(SuperviseEntity::getSuperviseTime);
        Page<SuperviseEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<SuperviseVO> voList = page.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        return PageResult.of(voList, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public List<SuperviseVO> listByWorkId(Long workId) {
        List<SuperviseEntity> list = this.lambdaQuery()
                .eq(SuperviseEntity::getDeleted, 0)
                .eq(SuperviseEntity::getWorkId, workId)
                .orderByDesc(SuperviseEntity::getSuperviseTime)
                .list();
        return list.stream().map(this::toVO).collect(Collectors.toList());
    }

    @Override
    public SuperviseVO getVOById(Long id) {
        SuperviseEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "督办记录不存在");
        }
        return toVO(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSupervise(SuperviseDTO dto) {
        SuperviseEntity entity = dto.toEntity();
        entity.setSuperviseTime(LocalDateTime.now());
        // 从网关Header自动填充当前督办人信息
        entity.setSuperviseBy(UserContext.getUserId());
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSupervise(SuperviseDTO dto) {
        SuperviseEntity entity = this.getById(dto.getId());
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "督办记录不存在");
        }
        BeanUtil.copyProperties(dto, entity, "id", "superviseTime", "createTime", "createBy", "deleted");
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSupervise(Long id) {
        SuperviseEntity entity = this.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            throw new BusinessException(404, "督办记录不存在");
        }
        this.removeById(id);
    }

    private SuperviseVO toVO(SuperviseEntity entity) {
        SuperviseVO vo = new SuperviseVO();
        BeanUtil.copyProperties(entity, vo);
        return vo;
    }
}
