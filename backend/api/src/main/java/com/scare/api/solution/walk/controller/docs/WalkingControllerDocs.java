package com.scare.api.solution.walk.controller.docs;


import com.scare.api.member.domain.Member;
import com.scare.api.solution.walk.controller.request.WalkingLogReq;
import com.scare.api.solution.walk.controller.response.WalkingDetailRes;
import com.scare.api.solution.walk.controller.response.WalkingLogListRes;
import com.scare.api.solution.walk.controller.response.WeeklyWalkingReportRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Walking", description = "Walking API")
public interface WalkingControllerDocs {

    @Operation(
            summary = "걷기 운동 기록 저장",
            description = "사용자의 걷기 운동 기록을 저장합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "걷기 운동 기록 저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    ResponseEntity<?> saveWalkingLog(
            @Parameter(
                    hidden = true,
                    description = "인증된 회원 정보 (시스템에서 자동 주입)"
            )
            Member member,

            @Parameter(description = "걷기 운동 기록 정보", required = true)
            WalkingLogReq walkingLogReq
    );

    @Operation(
            summary = "걷기 운동 기록 목록 조회",
            description = "사용자의 걷기 운동 기록 목록을 조회합니다"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = WalkingLogListRes.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    ResponseEntity<?> getWalkingLogList(
            @Parameter(
                    hidden = true,
                    description = "인증된 회원 정보 (시스템에서 자동 주입)"
            )
            Member member
    );

    @Operation(
            summary = "걷기 운동 상세 정보 조회",
            description = """
                특정 걷기 운동의 상세 정보를 조회합니다.
                - 회원의 특정 시점 걷기 운동 기록을 상세 조회
                - 시작 시간을 기준으로 해당 운동 기록을 식별
            """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = WalkingDetailRes.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "해당 시간에 시작된 걷기 운동 기록이 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    ResponseEntity<?> getWalkingDetail(
            @Parameter(
                    hidden = true,
                    description = "인증된 회원 정보 (시스템에서 자동 주입)"
            )
            Member member,

            @Parameter(
                    description = "걷기 운동 시작 시간",
                    example = "2024-10-31 14:30:00",
                    required = true
            )
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime startedAt
    );

    @Operation(
            summary = "주간 걷기 운동 리포트 조회",
            description = """
                회원별 주간 걷기 운동 종합 통계 리포트를 조회합니다.
                - 주간 범위: 요청 날짜가 속한 주의 월요일 00:00:00부터 일요일 23:59:59까지
                - 예시: 2024-10-28(월) ~ 2024-11-03(일)
                - 통계 데이터는 해당 회원의 기간 내 모든 걷기 운동 기록을 종합하여 제공
            """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = WeeklyWalkingReportRes.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    ResponseEntity<?> getWeeklyReport(
            @Parameter(
                    hidden = true,
                    description = "인증된 회원 정보 (시스템에서 자동 주입)"
            )
            Member member,

            @Parameter(
                    description = "조회할 날짜 (해당 날짜가 속한 주의 리포트가 조회됨)",
                    example = "2024-10-28",
                    required = true
            )
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate requestDate
    );
}