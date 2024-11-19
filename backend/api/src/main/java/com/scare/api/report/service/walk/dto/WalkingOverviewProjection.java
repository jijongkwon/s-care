package com.scare.api.report.service.walk.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

@Getter
public class WalkingOverviewProjection {

	private Integer totalWalkingTime;
	private Long totalWalkingCnt;
	private Double avgStressChange;

	@QueryProjection
	public WalkingOverviewProjection(Integer totalWalkingTime, Long totalWalkingCnt, Double avgStressChange) {
		this.totalWalkingTime = totalWalkingTime;
		this.totalWalkingCnt = totalWalkingCnt;
		this.avgStressChange = avgStressChange;
	}

}