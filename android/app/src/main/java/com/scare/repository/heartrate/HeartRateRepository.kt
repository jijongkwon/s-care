package com.scare.repository.heartrate

import com.scare.data.heartrate.database.AppDatabase
import com.scare.data.heartrate.database.entity.HeartRate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class HeartRateRepository @Inject constructor(
    private val appDatabase: AppDatabase
) {
    fun save(heartRate: HeartRate) {
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.getHeartRateDao().save(heartRate)
        }
    }

}