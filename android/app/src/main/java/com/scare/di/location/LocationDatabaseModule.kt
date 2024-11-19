package com.scare.di.location;

import android.content.Context
import com.scare.data.location.database.LocationDatabase
import com.scare.repository.location.LocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationDatabaseModule {

    @Singleton
    @Provides
    fun provideLocationDatabase(
        @ApplicationContext context: Context
    ): LocationDatabase? = LocationDatabase.getInstance(context)

    @Singleton
    @Provides
    fun provideLocationRepository(
        locationDatabase: LocationDatabase
    ): LocationRepository = LocationRepository(locationDatabase = locationDatabase)

}
