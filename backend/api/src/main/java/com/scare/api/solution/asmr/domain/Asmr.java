package com.scare.api.solution.asmr.domain;

import com.scare.api.core.domain.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "asmr")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Asmr extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "asmr_id")
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "audio_url", nullable = false)
	private String audioUrl;

	@Builder
	public Asmr(String name, String audioUrl) {
		this.name = name;
		this.audioUrl = audioUrl;
	}

}
