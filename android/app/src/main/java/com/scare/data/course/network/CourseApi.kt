package com.scare.data.course.network

import com.scare.data.course.dto.CourseDetailAPIResponseDTO
import com.scare.data.course.dto.CourseListAPIResponseDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CourseApi {
    @GET("/api/v1/walking/list")
    suspend fun getWalkingCourseList(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): CourseListAPIResponseDTO // 산책 코스 목록 조회

    @GET("/api/v1/walking/detail/{course-id}")
    suspend fun getWalkingCourseDetail(@Path("course-id") courseId: Long): CourseDetailAPIResponseDTO // 산책 코스 상세 조회
}
