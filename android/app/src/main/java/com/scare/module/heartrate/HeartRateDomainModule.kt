package com.scare.module.heartrate

import com.scare.repository.heartrate.HeartRateRepository
import com.scare.service.heartrate.HeartRateUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HeartRateDomainModule {

    @Singleton
    @Provides
    fun provideSaveHeartRateUseCase(
        heartRateRepository: HeartRateRepository
    ) = HeartRateUseCase(heartRateRepository)

}