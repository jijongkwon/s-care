package com.scare.api.solution.walk.service.command.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.scare.api.member.domain.Member;
import com.scare.api.solution.walk.controller.request.command.SaveWalkingCourseReq;
import com.scare.api.solution.walk.domain.WalkingCourse;
import com.scare.api.solution.walk.domain.WalkingDetail;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SaveWalkingCourseDto {

	private double distance;
	private double minStress;
	private double maxStress;
	private double healingStressAvg;
	private int startIdx;
	private int endIdx;
	private LocalDateTime startedAt;
	private LocalDateTime finishedAt;
	private List<Double> heartRates;
	private SaveWalkingCourseStressDto stressData;
	private List<SaveWalkingCourseLocationDto> locations;

	public static SaveWalkingCourseDto from(SaveWalkingCourseReq request) {
		return SaveWalkingCourseDto.builder()
			.distance(request.getDistance())
			.startedAt(request.getStartedAt())
			.finishedAt(request.getFinishedAt())
			.heartRates(request.getHeartRates())
			.locations(request.getLocations().stream()
				.map(SaveWalkingCourseLocationDto::from)
				.collect(Collectors.toList()))
			.build();
	}

	// to WalkingCourse
	public SaveWalkingCourseDto withStressData(SaveWalkingCourseStressDto stressData) {
		return SaveWalkingCourseDto.builder()
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
	public WalkingCourse toWalkingCourse(Member member) {
		return WalkingCourse.builder()
			.distance(this.distance)
			.minStress(this.stressData.getMinStress())
			.maxStress(this.stressData.getMaxStress())
			.startIdx(this.stressData.getStartIdx())
			.endIdx(this.stressData.getEndIdx())
			.startedAt(this.startedAt)
			.finishedAt(this.finishedAt)
			.member(member)
			.build();
	}

	public WalkingDetail toWalkingDetail(Long walkingCourseId, List<WalkingDetail.LocationPoint> locationPoints) {
		return WalkingDetail.builder()
			.id(walkingCourseId)
			.locationData(locationPoints)
			.build();
	}
}
