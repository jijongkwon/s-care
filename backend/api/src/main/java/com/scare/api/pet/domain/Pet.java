package com.scare.api.pet.domain;

import java.util.ArrayList;
import java.util.List;

import com.scare.api.core.domain.BaseTimeEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "pet")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Pet extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pet_id")
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
	List<PetCollection> collection = new ArrayList<>();

	@Builder
	public Pet(String name) {
		this.name = name;
	}

}
