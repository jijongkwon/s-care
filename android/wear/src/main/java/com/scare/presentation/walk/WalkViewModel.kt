package com.scare.presentation.walk

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.scare.datastore.IS_WALK_KEY
import com.scare.datastore.saveWalkStatus
import com.scare.datastore.walkStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class WalkViewModel(context: Context) : ViewModel() {

    private val _isWalk = MutableStateFlow(false)
    val isWalk: Flow<Boolean> = _isWalk

    init {
        // DataStore에서 산책 상태를 관찰
        viewModelScope.launch {
            context.walkStore.data.map { preferences ->
                preferences[IS_WALK_KEY] ?: false
            }.collect { isWalkStatus ->
                _isWalk.value = isWalkStatus
            }
        }
    }

    // 산책 상태 저장
    fun updateWalkStatus(context: Context, isWalk: Boolean) {
        saveWalkStatus(context, isWalk)
        _isWalk.value = isWalk
    }
}

class WalkViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WalkViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WalkViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}