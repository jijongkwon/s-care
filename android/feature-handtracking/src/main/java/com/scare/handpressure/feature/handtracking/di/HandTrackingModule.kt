package com.scare.handpressure.feature.handtracking.di

import android.content.Context
import com.scare.handpressure.feature.handtracking.domain.HandTrackingProcessor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HandTrackingModule {

    @Provides
    @Singleton
    fun provideHandTrackingProcessor(
        @ApplicationContext context: Context
    ): HandTrackingProcessor {
        return HandTrackingProcessor(context)
    }
}