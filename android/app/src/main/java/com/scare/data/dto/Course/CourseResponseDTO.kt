package com.scare.data.dto.Course

data class CourseResponseDTO(
    val courseId: Long,
    val startedAt: String,
    val finishedAt: String,
    val posList: List<PositionDTO>?,
    val startIdx: Int,
    val endIdx: Int,
    val maxStress: Double,
    val minStress: Double,
    val distance: Double
)