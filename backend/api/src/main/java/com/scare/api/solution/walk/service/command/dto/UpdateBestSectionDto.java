package com.scare.api.solution.walk.service.command.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateBestSectionDto {

	private Long courseId;
	private int maxStress;
	private int minStress;
	private Double healingStressAvg;
	private Integer startIdx;
	private Integer endIdx;

}
