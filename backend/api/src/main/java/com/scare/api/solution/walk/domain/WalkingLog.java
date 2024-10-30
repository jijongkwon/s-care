package com.scare.api.solution.walk.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "walking_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class WalkingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "walking_history_id")
    private Long id;

    @Column(name ="distance", nullable = false)
    private double distance;

    @Column(name = "min_stress_idx", nullable = false)
    private double minStressIdx;

    @Column(name = "max_stress_idx", nullable = false)
    private double maxStressIdx;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "finished_at", nullable = false)
    private LocalDateTime finishedAt;

    @Builder
    public WalkingLog(double distance, double minStressIdx, double maxStressIdx, LocalDateTime startedAt,
                      LocalDateTime finishedAt) {
        this.distance = distance;
        this.minStressIdx = minStressIdx;
        this.maxStressIdx = maxStressIdx;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
    }
}
