package com.scare.presentation.walk

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.scare.TAG
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

    private val dataClient = Wearable.getDataClient(context)

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

        sendToHandheldDevice(isWalk)
    }

    private fun sendToHandheldDevice(isWalk: Boolean) {
        val putDataReq = PutDataMapRequest.create("/walk").apply {
            dataMap.putBoolean("isWalk", isWalk)
            dataMap.putLong("timestamp", System.currentTimeMillis())  // Optional: add a timestamp
        }.asPutDataRequest()
            .setUrgent()

        dataClient.putDataItem(putDataReq).addOnSuccessListener {
            Log.d(TAG, "Walk state is sent to handheld device successfully")
        }.addOnFailureListener { e ->
            Log.e(TAG, "Failed to send walk state to handheld device", e)
        }
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