package com.scare.api.stress.service.query.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DailyStressQueryDto {

	private int stress;
	private String recordedAt;
	
}
