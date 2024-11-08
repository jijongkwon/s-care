package com.scare.service.heartrate

import com.scare.data.heartrate.database.entity.HeartRate
import com.scare.repository.heartrate.HeartRateRepository
import javax.inject.Inject

class HeartRateUseCase @Inject constructor(
    private val heartRateRepository: HeartRateRepository
) {

    fun save(heartRate: HeartRate) {
        heartRateRepository.save(heartRate)
    }

}