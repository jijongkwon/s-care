package com.scare.api.solution.walk.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
@Schema(description = "걷기 운동 기록 요청 DTO")
public class WalkingLogReq {

    @Schema(description = "산책 총 거리(km 단위)", example = "1.7")
    private double distance;

    @Schema(description = "가장 낮은 스트레스 지수값", example = "70.0")
    private double minStressIdx;

    @Schema(description = "stress 지수가 가장 높은 인덱스", example = "130.0")
    private double maxStressIdx;

    @Schema(description = "산책 시작 시간", example = "2024-10-31 14:30:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startedAt;

    @Schema(description = "산책 종료 시간", example = "2024-10-31 14:50:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime finishedAt;
}
