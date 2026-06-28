package com.gov.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.monitor.dto.DashboardStatDTO;
import com.gov.monitor.entity.DashboardStatEntity;
import com.gov.monitor.entity.EfficiencyStatEntity;
import com.gov.monitor.mapper.DashboardStatMapper;
import com.gov.monitor.mapper.EfficiencyStatMapper;
import com.gov.monitor.service.DashboardService;
import com.gov.monitor.vo.DashboardOverviewVO;
import com.gov.monitor.vo.DashboardTrendVO;
import com.gov.monitor.vo.DeptRankVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl extends ServiceImpl<DashboardStatMapper, DashboardStatEntity> implements DashboardService {

    private final EfficiencyStatMapper efficiencyStatMapper;

    @Override
    public DashboardOverviewVO getOverview() {
        LocalDate today = LocalDate.now();
        DashboardOverviewVO vo = new DashboardOverviewVO();

        Long todayTotal = this.lambdaQuery()
                .eq(DashboardStatEntity::getDeleted, 0)
                .eq(DashboardStatEntity::getStatDate, today)
                .eq(DashboardStatEntity::getStatType, "total")
                .list()
                .stream().mapToLong(e -> e.getStatValue() != null ? e.getStatValue() : 0L).sum();
        vo.setTodayTotal(todayTotal);

        Long inProgress = this.lambdaQuery()
                .eq(DashboardStatEntity::getDeleted, 0)
                .eq(DashboardStatEntity::getStatDate, today)
                .eq(DashboardStatEntity::getStatType, "finish")
                .list()
                .stream().mapToLong(e -> e.getStatValue() != null ? e.getStatValue() : 0L).sum();
        vo.setInProgress(todayTotal != null ? Math.max(0, todayTotal - inProgress) : 0L);
        vo.setCompleted(inProgress);

        BigDecimal satisfactionRate = this.lambdaQuery()
                .eq(DashboardStatEntity::getDeleted, 0)
                .eq(DashboardStatEntity::getStatDate, today)
                .eq(DashboardStatEntity::getStatType, "satisfaction")
                .list()
                .stream()
                .filter(e -> e.getStatValue() != null)
                .map(e -> BigDecimal.valueOf(e.getStatValue()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        vo.setSatisfactionRate(satisfactionRate);

        return vo;
    }

    @Override
    public List<DashboardTrendVO> getTrend(Integer days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days != null ? days : 7);

        List<DashboardStatEntity> list = this.lambdaQuery()
                .eq(DashboardStatEntity::getDeleted, 0)
                .eq(DashboardStatEntity::getStatType, "total")
                .ge(DashboardStatEntity::getStatDate, startDate)
                .le(DashboardStatEntity::getStatDate, endDate)
                .orderByAsc(DashboardStatEntity::getStatDate)
                .list();

        Map<LocalDate, Long> dateMap = list.stream()
                .collect(Collectors.groupingBy(DashboardStatEntity::getStatDate,
                        Collectors.summingLong(e -> e.getStatValue() != null ? e.getStatValue() : 0L)));

        List<DashboardTrendVO> result = new ArrayList<>();
        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            DashboardTrendVO vo = new DashboardTrendVO();
            vo.setStatDate(current);
            vo.setDailyCount(dateMap.getOrDefault(current, 0L));
            result.add(vo);
            current = current.plusDays(1);
        }
        return result;
    }

    @Override
    public List<DeptRankVO> getDeptRank() {
        LambdaQueryWrapper<EfficiencyStatEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EfficiencyStatEntity::getDeleted, 0);
        wrapper.orderByDesc(EfficiencyStatEntity::getTotalCount);
        List<EfficiencyStatEntity> list = efficiencyStatMapper.selectList(wrapper);

        Map<Long, List<EfficiencyStatEntity>> deptMap = list.stream()
                .filter(e -> e.getDeptId() != null)
                .collect(Collectors.groupingBy(EfficiencyStatEntity::getDeptId));

        return deptMap.entrySet().stream().map(entry -> {
            DeptRankVO vo = new DeptRankVO();
            vo.setDeptId(entry.getKey());
            String deptName = entry.getValue().stream()
                    .filter(e -> e.getDeptName() != null)
                    .map(EfficiencyStatEntity::getDeptName)
                    .findFirst().orElse("");
            vo.setDeptName(deptName);
            long totalCount = entry.getValue().stream()
                    .mapToLong(e -> e.getTotalCount() != null ? e.getTotalCount() : 0L).sum();
            vo.setTotalCount(totalCount);
            double avgTime = entry.getValue().stream()
                    .filter(e -> e.getAvgTime() != null)
                    .mapToInt(EfficiencyStatEntity::getAvgTime)
                    .average().orElse(0);
            vo.setAvgTime((int) avgTime);
            double satisfactionAvg = entry.getValue().stream()
                    .filter(e -> e.getSatisfactionAvg() != null)
                    .mapToDouble(e -> e.getSatisfactionAvg().doubleValue())
                    .average().orElse(0);
            vo.setSatisfactionAvg(BigDecimal.valueOf(satisfactionAvg));
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addStat(DashboardStatDTO dto) {
        DashboardStatEntity entity = dto.toEntity();
        this.save(entity);
    }
}
