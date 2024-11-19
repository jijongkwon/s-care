package com.scare.data.location.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.scare.data.location.database.entity.Location

@Dao
interface LocationDao {

    @Insert
    fun save(vararg location: Location)

    @Query("SELECT * FROM location WHERE created_at BETWEEN :startDate AND :endDate ORDER BY created_at DESC")
    fun getLocations(startDate: String, endDate: String): List<Location>

    @Query("DELETE FROM location")
    fun deleteAllLocations()

}