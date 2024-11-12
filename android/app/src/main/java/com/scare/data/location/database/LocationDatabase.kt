package com.scare.data.location.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.scare.data.core.TimeConverters
import com.scare.data.location.dao.LocationDao
import com.scare.data.location.database.entity.Location

@Database(entities = [Location::class], version = 1)
@TypeConverters(TimeConverters::class)
abstract class LocationDatabase : RoomDatabase() {

    abstract fun getLocationDao(): LocationDao

    companion object {
        private const val DATABASE_NAME = "scare_location_database"

        @Volatile
        private var locationDatabase: LocationDatabase? = null

        fun getInstance(context: Context): LocationDatabase {
            return locationDatabase ?: synchronized(this) {
                locationDatabase ?: buildDatabase(context).also { locationDatabase = it }
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            LocationDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}