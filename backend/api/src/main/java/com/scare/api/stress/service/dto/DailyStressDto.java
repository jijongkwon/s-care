package com.scare.api.stress.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DailyStressDto {

	private int stress;
	private String recordedAt;

}
