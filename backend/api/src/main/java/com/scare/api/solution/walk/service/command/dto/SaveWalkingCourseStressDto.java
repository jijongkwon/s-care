package com.scare.api.solution.walk.service.command.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SaveWalkingCourseStressDto {
	@JsonProperty("max_stress")
	private double maxStress;

	@JsonProperty("min_stress")
	private double minStress;

	@JsonProperty("healing_stress_avg")
	private double healingStressAvg;

	@JsonProperty("start_idx")
	private int startIdx;

	@JsonProperty("end_idx")
	private int endIdx;

	@JsonProperty("stress_indices")
	private List<Double> stressIndices;
}
