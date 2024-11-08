package com.scare.data.course.repository

import com.scare.data.RetrofitClient
import com.scare.data.course.dto.CourseDetailAPIResponseDTO
import com.scare.data.course.dto.CourseListAPIResponseDTO
import com.scare.data.course.network.CourseApi

class CourseRepository {

    val courseApi: CourseApi by lazy {
        RetrofitClient.retrofit.create(CourseApi::class.java)
    }

    // 산책 코스 목록 가져오기
    suspend fun getWalkingCourseList(page: Int, size: Int): CourseListAPIResponseDTO {
        return courseApi.getWalkingCourseList(page, size)
    }

    // 산책 코스 상세 정보 가져오기
    suspend fun getWalkingCourseDetail(courseId: Long): CourseDetailAPIResponseDTO {
        return courseApi.getWalkingCourseDetail(courseId)
    }
}
