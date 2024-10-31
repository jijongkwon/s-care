package com.scare.api.solution.walk.repository;

import com.scare.api.solution.walk.domain.WalkingLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkingLogRepository extends JpaRepository<WalkingLog, Long> {
}
