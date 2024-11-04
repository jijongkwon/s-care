package com.scare.api.report.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scare.api.core.jwt.dto.CustomUserDetails;
import com.scare.api.core.template.response.BaseResponse;
import com.scare.api.report.controller.response.WeeklyReportRes;
import com.scare.api.report.facade.ReportFacade;
import com.scare.api.report.service.dto.ReportDto;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/report")
@RestController
@RequiredArgsConstructor
public class ReportController {

	private final ReportFacade reportFacade;

	@GetMapping("/weekly")
	public ResponseEntity<BaseResponse<WeeklyReportRes>> getWeeklyReport(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam("from") String from,
		@RequestParam("to") String to
	) {
		List<ReportDto> reports = reportFacade.getReports(userDetails.getMemberId(), from, to);
		// TODO: 지난주, 이번주 스트레스 조회
		return ResponseEntity.ok(BaseResponse.ofSuccess(WeeklyReportRes.builder()
			.lastWeekStress(null)
			.currentWeekStress(null)
			.reports(reports)
			.build()));
	}
}
