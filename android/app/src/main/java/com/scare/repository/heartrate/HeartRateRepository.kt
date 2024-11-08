package com.scare.repository.heartrate

import com.scare.data.heartrate.database.entity.HeartRate

interface HeartRateRepository {

    fun save(heartRate: HeartRate)

}