package com.gov.monitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gov.monitor.entity.EfficiencyStatEntity;
import com.gov.monitor.mapper.EfficiencyStatMapper;
import com.gov.monitor.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final EfficiencyStatMapper efficiencyStatMapper;

    @Override
    public void exportEfficiency(HttpServletResponse response, String startDate, String endDate, Long deptId) {
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
        List<EfficiencyStatEntity> list = efficiencyStatMapper.selectList(wrapper);

        try (Workbook workbook = new HSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("效能统计报表");

            Row header = sheet.createRow(0);
            String[] headers = {"部门名称", "统计日期", "总办件数", "完成数", "超期数", "驳回数",
                    "平均时长(分钟)", "最长时长(分钟)", "最短时长(分钟)", "平均满意度", "黄牌数", "红牌数"};
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }

            int rowIndex = 1;
            for (EfficiencyStatEntity e : list) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(e.getDeptName() != null ? e.getDeptName() : "");
                row.createCell(1).setCellValue(e.getStatDate() != null ? e.getStatDate().toString() : "");
                row.createCell(2).setCellValue(e.getTotalCount() != null ? e.getTotalCount() : 0);
                row.createCell(3).setCellValue(e.getFinishCount() != null ? e.getFinishCount() : 0);
                row.createCell(4).setCellValue(e.getTimeoutCount() != null ? e.getTimeoutCount() : 0);
                row.createCell(5).setCellValue(e.getRejectCount() != null ? e.getRejectCount() : 0);
                row.createCell(6).setCellValue(e.getAvgTime() != null ? e.getAvgTime() : 0);
                row.createCell(7).setCellValue(e.getMaxTime() != null ? e.getMaxTime() : 0);
                row.createCell(8).setCellValue(e.getMinTime() != null ? e.getMinTime() : 0);
                row.createCell(9).setCellValue(e.getSatisfactionAvg() != null ? e.getSatisfactionAvg().doubleValue() : 0);
                row.createCell(10).setCellValue(e.getYellowCount() != null ? e.getYellowCount() : 0);
                row.createCell(11).setCellValue(e.getRedCount() != null ? e.getRedCount() : 0);
            }

            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            String fileName = URLEncoder.encode("效能统计报表.xls", StandardCharsets.UTF_8.toString());
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            try (OutputStream os = response.getOutputStream()) {
                workbook.write(os);
                os.flush();
            }
        } catch (Exception e) {
            throw new RuntimeException("导出Excel失败", e);
        }
    }
}
