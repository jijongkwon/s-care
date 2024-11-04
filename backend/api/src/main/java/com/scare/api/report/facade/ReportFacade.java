package com.scare.api.report.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.scare.api.report.service.ReportService;
import com.scare.api.report.service.dto.ReportDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public final class ReportFacade {

	private final List<ReportService> reportServices = new ArrayList<>();

	public List<ReportDto> getReports(Long memberId, String startDate, String endDate) {
		return reportServices.stream()
			.map(reportService -> reportService.getReport(memberId, startDate, endDate))
			.collect(Collectors.toList());
	}

}
