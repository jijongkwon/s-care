package com.scare.api.report.service.walk.dto;

import java.util.List;

import com.scare.api.report.service.dto.ReportDto;
import com.scare.api.report.service.dto.ReportType;
import com.scare.api.solution.walk.service.query.dto.Pos;

import lombok.Builder;
import lombok.Getter;

@Getter
public class WalkingReportDto extends ReportDto {

	private long walkingCnt;
	private long totalWalkingTime;
	private double avgStressChange;
	private Integer startIdx;
	private Integer endIdx;
	List<Pos> posList;

	@Builder
	public WalkingReportDto(long walkingCnt, long totalWalkingTime, double avgStressChange,
		Integer startIdx, Integer endIdx, List<Pos> posList) {
		super(ReportType.WALKING);
		this.walkingCnt = walkingCnt;
		this.totalWalkingTime = totalWalkingTime;
		this.avgStressChange = avgStressChange;
		this.startIdx = startIdx;
		this.endIdx = endIdx;
		this.posList = posList;
	}

}
