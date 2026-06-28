package com.gov.monitor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.monitor.dto.EfficiencyStatDTO;
import com.gov.monitor.dto.SatisfactionStatDTO;
import com.gov.monitor.entity.EfficiencyStatEntity;
import com.gov.monitor.entity.SatisfactionStatEntity;
import com.gov.monitor.mapper.EfficiencyStatMapper;
import com.gov.monitor.mapper.SatisfactionStatMapper;
import com.gov.monitor.service.StatService;
import com.gov.monitor.vo.EfficiencyStatVO;
import com.gov.monitor.vo.SatisfactionStatVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatServiceImpl extends ServiceImpl<EfficiencyStatMapper, EfficiencyStatEntity> implements StatService {

    private final SatisfactionStatMapper satisfactionStatMapper;

    @Override
    public List<EfficiencyStatVO> getEfficiency(Long deptId, String startDate, String endDate) {
        LambdaQueryWrapper<EfficiencyStatEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EfficiencyStatEntity::getDeleted, 0);
        wrapper.eq(deptId != null, EfficiencyStatEntity::getDeptId, deptId);
        if (startDate != null && !startDate.isEmpty()) {
            wrapper.ge(EfficiencyStatEntity::getStatDate, LocalDate.parse(startDate));
        }
        if (endDate != null && !endDate.isEmpty()) {
            wrapper.le(EfficiencyStatEntity::getStatDate, LocalDate.parse(endDate));
        }
        wrapper.orderByDesc(EfficiencyStatEntity::getStatDate);
        List<EfficiencyStatEntity> list = this.list(wrapper);
        return list.stream().map(this::toVO).collect(Collectors.toList());
    }

    @Override
    public List<SatisfactionStatVO> getSatisfaction(Long deptId, String startDate, String endDate) {
        LambdaQueryWrapper<SatisfactionStatEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SatisfactionStatEntity::getDeleted, 0);
        wrapper.eq(deptId != null, SatisfactionStatEntity::getDeptId, deptId);
        if (startDate != null && !startDate.isEmpty()) {
            wrapper.ge(SatisfactionStatEntity::getStatDate, LocalDate.parse(startDate));
        }
        if (endDate != null && !endDate.isEmpty()) {
            wrapper.le(SatisfactionStatEntity::getStatDate, LocalDate.parse(endDate));
        }
        wrapper.orderByDesc(SatisfactionStatEntity::getStatDate);
        List<SatisfactionStatEntity> list = satisfactionStatMapper.selectList(wrapper);
        return list.stream().map(this::toVO).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addEfficiencyStat(EfficiencyStatDTO dto) {
        EfficiencyStatEntity entity = dto.toEntity();
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSatisfactionStat(SatisfactionStatDTO dto) {
        SatisfactionStatEntity entity = dto.toEntity();
        satisfactionStatMapper.insert(entity);
    }

    private EfficiencyStatVO toVO(EfficiencyStatEntity entity) {
        EfficiencyStatVO vo = new EfficiencyStatVO();
        BeanUtil.copyProperties(entity, vo);
        return vo;
    }

    private SatisfactionStatVO toVO(SatisfactionStatEntity entity) {
        SatisfactionStatVO vo = new SatisfactionStatVO();
        BeanUtil.copyProperties(entity, vo);
        return vo;
    }
}
