package com.scare.api.report.service.dto;

public abstract class ReportDto {

	private final ReportType type;

	public ReportDto(ReportType type) {
		this.type = type;
	}

}
