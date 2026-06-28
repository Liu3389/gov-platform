package com.gov.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.monitor.dto.DashboardStatDTO;
import com.gov.monitor.entity.DashboardStatEntity;
import com.gov.monitor.vo.DashboardOverviewVO;
import com.gov.monitor.vo.DashboardTrendVO;
import com.gov.monitor.vo.DeptRankVO;

import java.util.List;

public interface DashboardService extends IService<DashboardStatEntity> {

    DashboardOverviewVO getOverview();

    List<DashboardTrendVO> getTrend(Integer days);

    List<DeptRankVO> getDeptRank();

    void addStat(DashboardStatDTO dto);
}
