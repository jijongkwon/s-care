package com.scare.api.stress.service.command.dto;

import com.scare.api.report.service.dto.ReportDto;
import com.scare.api.report.service.dto.ReportType;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StressReportDto extends ReportDto {

	private Double lastWeekStress;
	private Double currentWeekStress;

	@Builder
	public StressReportDto(Double lastWeekStress, Double currentWeekStress) {
		super(ReportType.STRESS);
		this.lastWeekStress = lastWeekStress;
		this.currentWeekStress = currentWeekStress;
	}

}
