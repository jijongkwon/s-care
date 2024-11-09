package com.scare.data.heartrate.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.scare.data.heartrate.database.entity.HeartRate

@Dao
interface HeartRateDao {

    @Insert
    fun save(vararg heartRate: HeartRate)

    @Query("SELECT * FROM heart_rate ORDER BY created_at DESC LIMIT 10")
    fun getRecentHeartRates(): List<HeartRate>

}