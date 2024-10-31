package com.scare.api.solution.walk.domain;

import java.time.LocalDateTime;

import com.scare.api.member.domain.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "walking_course")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class WalkingCourse {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "walking_course_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Column(name = "distance", nullable = false)
	private double distance;

	@Column(name = "min_stress", nullable = false)
	private double minStress;

	@Column(name = "max_stress", nullable = false)
	private double maxStress;

	@Column(name = "healing_stress_avg", nullable = false)
	private double healingStressAvg;

	@Column(name = "start_idx")
	private Integer startIdx;

	@Column(name = "end_idx")
	private Integer endIdx;

	@Column(name = "started_at", nullable = false)
	private LocalDateTime startedAt;

	@Column(name = "finished_at", nullable = false)
	private LocalDateTime finishedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	Member member;

	@Builder
	public WalkingCourse(double distance, double minStress, double maxStress, double healingStressAvg, Integer startIdx,
		Integer endIdx, LocalDateTime startedAt, LocalDateTime finishedAt, Member member) {
		this.distance = distance;
		this.minStress = minStress;
		this.maxStress = maxStress;
		this.healingStressAvg = healingStressAvg;
		this.startIdx = startIdx;
		this.endIdx = endIdx;
		this.startedAt = startedAt;
		this.finishedAt = finishedAt;
		this.member = member;
	}
}
