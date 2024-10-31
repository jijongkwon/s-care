package com.scare.api.solution.walk.service;

import com.scare.api.solution.walk.repository.WalkingLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalkingLogService {

    private final WalkingLogRepository walkingLogRepository;
}
