package com.scare.api.solution.walk.service.command.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.scare.api.solution.walk.controller.request.command.WalkingCourseReq;
import com.scare.api.solution.walk.domain.WalkingCourse;
import com.scare.api.solution.walk.domain.WalkingDetail;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WalkingCourseDto {

	private double distance;
	private double minStress;
	private double maxStress;
	private double healingStressAvg;
	private int startIdx;
	private int endIdx;
	private LocalDateTime startedAt;
	private LocalDateTime finishedAt;
	private List<Double> heartRates;
	private WalkingCourseStressDto stressData;
	private List<WalkingCourseLocationDto> locations;

	public static WalkingCourseDto from(WalkingCourseReq request) {
		return WalkingCourseDto.builder()
			.distance(request.getDistance())
			.startedAt(request.getStartedAt())
			.finishedAt(request.getFinishedAt())
			.heartRates(request.getHeartRates())
			.locations(request.getLocations().stream()
				.map(WalkingCourseLocationDto::from)
				.collect(Collectors.toList()))
			.build();
	}

	// to WalkingCourse
	public WalkingCourseDto withStressData(WalkingCourseStressDto stressData) {
		return WalkingCourseDto.builder()
			.distance(this.distance)
			.minStress(stressData.getMinStress())
			.maxStress(stressData.getMaxStress())
			.healingStressAvg(stressData.getHealingStressAvg())
			.startIdx(stressData.getStartIdx())
			.endIdx(stressData.getEndIdx())
			.startedAt(this.startedAt)
			.finishedAt(this.finishedAt)
			.heartRates(this.heartRates)
			.locations(this.locations)
			.stressData(stressData)
			.build();
	}

	// to WalkingDetail
	public WalkingCourse toWalkingCourse() {
		return WalkingCourse.builder()
			.distance(this.distance)
			.minStress(this.stressData.getMinStress())
			.maxStress(this.stressData.getMaxStress())
			.startIdx(this.stressData.getStartIdx())
			.endIdx(this.stressData.getEndIdx())
			.startedAt(this.startedAt)
			.finishedAt(this.finishedAt)
			.build();
	}

	public WalkingDetail toWalkingDetail(Long walkingCourseId) {
		List<WalkingDetail.LocationPoint> locationPoints = new ArrayList<>();
		List<Double> stressIndices = this.stressData.getStressIndices();

		for (int i = 3; i < this.locations.size(); i++) {
			WalkingCourseLocationDto location = this.locations.get(i);
			double stressIndex = stressIndices.get(i);

			locationPoints.add(WalkingDetail.LocationPoint.builder()
				.latitude(location.getLatitude())
				.longitude(location.getLongitude())
				.stressIndex(stressIndex)
				.createdAt(location.getCreatedAt())
				.build());
		}

		return WalkingDetail.builder()
			.id(walkingCourseId)
			.locationData(locationPoints)
			.build();
	}
}
