package com.scare.ui.mobile.viewmodel.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scare.data.course.dto.CourseResponseDTO
import com.scare.data.course.repository.CourseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CourseViewModel(private val courseRepository: CourseRepository) : ViewModel() {

    private val _courseList = MutableStateFlow<List<CourseResponseDTO>>(emptyList())
    val courseList: StateFlow<List<CourseResponseDTO>> get() = _courseList

    private val _courseDetail = MutableStateFlow<CourseResponseDTO?>(null)
    val courseDetail: StateFlow<CourseResponseDTO?> get() = _courseDetail

    // 산책 코스 목록 조회
    fun loadWalkingCourseList() {
        viewModelScope.launch {
            _courseList.value = courseRepository.getWalkingCourseList()
        }
    }

    // 특정 산책 코스의 상세 정보 조회
    fun loadWalkingCourseDetail(courseId: Long) {
        viewModelScope.launch {
            _courseDetail.value = courseRepository.getWalkingCourseDetail(courseId)
        }
    }
}
