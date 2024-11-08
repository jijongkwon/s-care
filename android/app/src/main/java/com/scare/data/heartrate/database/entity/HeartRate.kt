package com.scare.data.heartrate.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "heart_rate")
data class HeartRate(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "heart_rate_id")
    val id: Long = 0,

    @ColumnInfo(name = "heart_rate")
    val heartRate: Double,

    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime

)

