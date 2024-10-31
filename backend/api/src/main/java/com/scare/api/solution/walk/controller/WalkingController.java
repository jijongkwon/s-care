package com.scare.api.solution.walk.controller;

import com.scare.api.member.domain.Member;
import com.scare.api.solution.walk.controller.docs.WalkingControllerDocs;
import com.scare.api.solution.walk.controller.request.WalkingLogReq;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/walking")
public class WalkingController implements WalkingControllerDocs {

    @Override
    public ResponseEntity<?> saveWalkingLog(Member member, WalkingLogReq walkingLogReq) {
        return null;
    }

    @Override
    public ResponseEntity<?> getWalkingLogList(Member member) {
        return null;
    }

    @Override
    public ResponseEntity<?> getWalkingDetail(Member member, LocalDateTime startedAt) {
        return null;
    }

    @Override
    public ResponseEntity<?> getWeeklyReport(Member member, LocalDate requestDate) {
        return null;
    }
}