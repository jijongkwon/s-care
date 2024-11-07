package com.scare.data.heartrate.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.scare.data.heartrate.dao.HeartRateDao
import com.scare.data.heartrate.database.entity.HeartRate

@Database(entities = [HeartRate::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun heartRateDao(): HeartRateDao
    
}