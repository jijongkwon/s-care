package com.scare.data.course.network

import com.scare.data.course.dto.CourseResponseDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface CourseApi {
    @GET("/api/v1/walking/list")
    suspend fun getWalkingCourseList(): List<CourseResponseDTO> // 산책 코스 목록 조회

    @GET("/api/v1/walking/detail/{course-id}")
    suspend fun getWalkingCourseDetail(@Path("course-id") courseId: Long): CourseResponseDTO // 산책 코스 상세 조회
}
