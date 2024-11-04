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
	private LocalDateTime startedAt;
	private LocalDateTime finishedAt;
	private List<Integer> heartRates;
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

	public SaveWalkingCourseDto withStressData(SaveWalkingCourseStressDto stressData) {
		return SaveWalkingCourseDto.builder()
			.distance(this.distance)
			.startedAt(this.startedAt)
			.finishedAt(this.finishedAt)
			.heartRates(this.heartRates)
			.locations(this.locations)
			.stressData(stressData)
			.build();
	}

	public WalkingCourse toWalkingCourseWithFastApi(Member member) {
		return WalkingCourse.builder()
			.distance(this.distance)
			.minStress(this.stressData.getMinStress())
			.maxStress(this.stressData.getMaxStress())
			.startIdx(this.stressData.getStartIdx())
			.endIdx(this.stressData.getEndIdx())
			.healingStressAvg(this.stressData.getHealingStressAvg())
			.startedAt(this.startedAt)
			.finishedAt(this.finishedAt)
			.member(member)
			.build();
	}

	public WalkingCourse toWalkingCourseDefault(Member member) {
		return WalkingCourse.builder()
			.distance(this.distance)
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
