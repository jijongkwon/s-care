package com.scare.api.report.service;

import java.time.LocalDateTime;

import com.scare.api.report.service.dto.ReportDto;

public interface ReportService {

	ReportDto getReport(Long memberId, LocalDateTime startDate, LocalDateTime endDate);

}
