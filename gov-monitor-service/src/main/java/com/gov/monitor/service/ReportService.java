package com.gov.monitor.service;

import javax.servlet.http.HttpServletResponse;

public interface ReportService {

    void exportEfficiency(HttpServletResponse response, String startDate, String endDate, Long deptId);
}
