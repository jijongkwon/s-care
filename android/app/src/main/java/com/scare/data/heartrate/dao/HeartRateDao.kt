package com.scare.data.heartrate.dao

import androidx.room.Dao
import androidx.room.Insert
import com.scare.data.heartrate.database.entity.HeartRate

@Dao
interface HeartRateDao {

    @Insert
    fun save(vararg heartRate: HeartRate)

}