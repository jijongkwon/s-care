package com.scare.di.heartrate

import android.content.Context
import com.scare.data.heartrate.database.AppDatabase
import com.scare.repository.heartrate.HeartRateRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase? = AppDatabase.getInstance(context)

    @Singleton
    @Provides
    fun provideHeartRateRepository(
        appDatabase: AppDatabase
    ): HeartRateRepository = HeartRateRepository(appDatabase = appDatabase)

}