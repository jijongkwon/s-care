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
import com.scare.api.report.facade.ReportFacade;
import com.scare.api.report.service.dto.ReportDto;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/v1/report")
@RestController
@RequiredArgsConstructor
public class ReportController {

	private final ReportFacade reportFacade;

	@GetMapping("/weekly")
	public ResponseEntity<BaseResponse<List<ReportDto>>> getWeeklyReport(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam("from") String from,
		@RequestParam("to") String to
	) {
		List<ReportDto> reports = reportFacade.getReports(userDetails.getMemberId(), from, to);
		return ResponseEntity.ok(BaseResponse.ofSuccess(reports));
	}

}
