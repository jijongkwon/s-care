package com.scare.data.course.repository

import com.scare.data.RetrofitClient
import com.scare.data.course.dto.CourseResponseDTO
import com.scare.data.course.network.CourseApi

class CourseRepository {

    val courseApi: CourseApi by lazy {
        RetrofitClient.retrofit.create(CourseApi::class.java)
    }

    // 산책 코스 목록 가져오기
    suspend fun getWalkingCourseList(): List<CourseResponseDTO> {
        return courseApi.getWalkingCourseList()
    }

    // 산책 코스 상세 정보 가져오기
    suspend fun getWalkingCourseDetail(courseId: Long): CourseResponseDTO {
        return courseApi.getWalkingCourseDetail(courseId)
    }
}
