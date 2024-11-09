package com.scare.data.heartrate.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.scare.data.core.TimeConverters
import com.scare.data.heartrate.dao.HeartRateDao
import com.scare.data.heartrate.database.entity.HeartRate

@Database(entities = [HeartRate::class], version = 1)
@TypeConverters(TimeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getHeartRateDao(): HeartRateDao

    companion object {
        private const val DATABASE_NAME = "scare_database"

        @Volatile
        private var appDatabase: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return appDatabase ?: synchronized(this) {
                appDatabase ?: buildDatabase(context).also { appDatabase = it }
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}