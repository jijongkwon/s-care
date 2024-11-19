package com.scare.data.location.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "location")
data class Location(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "location_id")
    val id: Long = 0,

    @ColumnInfo(name = "latitude")
    val latitude: Double,

    @ColumnInfo(name = "longitude")
    val longitude: Double,

    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime

)

