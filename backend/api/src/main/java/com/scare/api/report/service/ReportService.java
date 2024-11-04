package com.scare.api.report.service;

import com.scare.api.report.service.dto.ReportDto;

public interface ReportService {

	ReportDto getReport(Long memberId, String startDate, String endDate);
	
}
