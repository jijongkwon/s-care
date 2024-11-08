package com.scare.service.heartrate

import android.content.Context
import com.scare.data.heartrate.dao.HeartRateDao
import com.scare.data.heartrate.database.AppDatabase
import com.scare.data.heartrate.database.entity.HeartRate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class SaveHeartRateService {

    private lateinit var db: AppDatabase
    private lateinit var heartRateDao: HeartRateDao

    fun saveHeartRate(context: Context, heartRate: Double) {

        val heartRateEntity = HeartRate(
            heartRate = heartRate,
            createdAt = LocalDateTime.now()
        )

        db = AppDatabase.getInstance(context)!!
        heartRateDao = db.getHeartRateDao()

        saveHearRate(heartRateEntity)

    }

    private fun saveHearRate(heartRateEntity: HeartRate) {
        CoroutineScope(Dispatchers.IO).launch {
            heartRateDao.save(heartRateEntity)
        }
    }

}