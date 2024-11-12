package com.scare.api.solution.walk.service.command.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SaveWalkingCourseStressDto {
	@JsonProperty("max_stress")
	private Double maxStress;

	@JsonProperty("min_stress")
	private Double minStress;

	@JsonProperty("healing_stress_avg")
	private Double healingStressAvg;

	@JsonProperty("start_idx")
	private Integer startIdx;

	@JsonProperty("end_idx")
	private Integer endIdx;

	@JsonProperty("stress_indices")
	private List<Double> stressIndices;
}
