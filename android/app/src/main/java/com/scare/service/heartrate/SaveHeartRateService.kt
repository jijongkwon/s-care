package com.scare.service.heartrate

import com.scare.data.heartrate.dao.HeartRateDao
import com.scare.data.heartrate.database.entity.HeartRate
import java.time.LocalDateTime
import javax.inject.Inject

class SaveHeartRateService @Inject constructor(
    private val heartRateDao: HeartRateDao
) {

    suspend fun saveHeartRate(heartRate: Double) {
        val heartRateEntity = HeartRate(
            heartRate = heartRate,
            createdAt = LocalDateTime.now()
        )
        heartRateDao.save(heartRateEntity)
    }

}