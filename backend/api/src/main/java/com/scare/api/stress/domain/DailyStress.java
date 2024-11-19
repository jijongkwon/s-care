package com.scare.api.stress.domain;

import java.time.LocalDate;

import com.scare.api.core.domain.BaseTimeEntity;
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

@Table(name = "daily_stress")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class DailyStress extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Column(name = "stress", nullable = false)
	private int stress;

	@Column(name = "recorded_at", nullable = false)
	private LocalDate recordedAt;

	@Builder
	public DailyStress(Member member, int stress, LocalDate recordedAt) {
		this.member = member;
		this.stress = stress;
		this.recordedAt = recordedAt;
	}
	
}
