package com.scare.api.report.facade;

import static com.scare.api.core.util.DateConverter.*;

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

	public List<ReportDto> getReports(Long memberId, String from, String to) {
		return reportServices.stream()
			.map(reportService -> reportService.getReport(memberId, convertToStartOfDay(from), convertToEndOfDay(to)))
			.collect(Collectors.toList());
	}

}
