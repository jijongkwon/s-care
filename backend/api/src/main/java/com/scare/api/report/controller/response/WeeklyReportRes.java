package com.scare.api.report.controller.response;

import java.util.List;

import com.scare.api.report.service.dto.ReportDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WeeklyReportRes {

	private List<ReportDto> reports;
	
}
