package com.scare.api.stress.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scare.api.stress.domain.DailyStress;

public interface DailyStressRepository extends JpaRepository<DailyStress, Long> {

}
