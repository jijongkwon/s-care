package com.scare.ui.mobile.viewmodel.course

import android.util.Log
import androidx.lifecycle.ViewModel
import com.scare.data.course.dto.CourseResponseDTO
import com.scare.data.course.repository.CourseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CourseViewModel(private val courseRepository: CourseRepository) : ViewModel() {

    private val _courseList = MutableStateFlow<List<CourseResponseDTO>>(emptyList())
    val courseList: StateFlow<List<CourseResponseDTO>> get() = _courseList

    private val _courseDetail = MutableStateFlow<CourseResponseDTO?>(null)
    val courseDetail: StateFlow<CourseResponseDTO?> get() = _courseDetail

    // 산책 코스 목록 조회
    suspend fun fetchWalkingCourseList(page: Int, size: Int) {
        try {
            _courseList.value = courseRepository.getWalkingCourseList(page, size).data
        } catch (e: Exception) {
            Log.e("CourseViewModel", "Error fetching course list", e)
        }
    }

    // 특정 산책 코스의 상세 정보 조회
    suspend fun fetchWalkingCourseDetail(courseId: Long) {
        try {
            _courseDetail.value = courseRepository.getWalkingCourseDetail(courseId).data
        } catch (e: Exception) {
            Log.e("CourseViewModel", "Error fetching course detail", e)
        }
    }
}
