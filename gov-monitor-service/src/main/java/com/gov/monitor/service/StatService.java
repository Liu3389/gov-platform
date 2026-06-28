package com.gov.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.monitor.dto.EfficiencyStatDTO;
import com.gov.monitor.dto.SatisfactionStatDTO;
import com.gov.monitor.entity.EfficiencyStatEntity;
import com.gov.monitor.vo.EfficiencyStatVO;
import com.gov.monitor.vo.SatisfactionStatVO;

import java.util.List;

public interface StatService extends IService<EfficiencyStatEntity> {

    List<EfficiencyStatVO> getEfficiency(Long deptId, String startDate, String endDate);

    List<SatisfactionStatVO> getSatisfaction(Long deptId, String startDate, String endDate);

    void addEfficiencyStat(EfficiencyStatDTO dto);

    void addSatisfactionStat(SatisfactionStatDTO dto);
}
