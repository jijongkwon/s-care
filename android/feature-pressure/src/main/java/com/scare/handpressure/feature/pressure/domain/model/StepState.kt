package com.scare.handpressure.feature.pressure.domain.model

enum class StepState {
    NOT_STARTED,
    DETECTING_POSITION,
    POSITION_INCORRECT,
    HOLDING_POSITION,
    COMPLETED,
    SKIPPED,
    COMPLETED_ALL
}