package com.scare.repository.location

import com.scare.data.location.database.LocationDatabase
import com.scare.data.location.database.entity.Location
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val locationDatabase: LocationDatabase
) {
    fun save(location: Location) {
        CoroutineScope(Dispatchers.IO).launch {
            locationDatabase.getLocationDao().save(location)
        }
    }

    fun getLocations(startDate: String, endDate: String): List<Location> {
        return locationDatabase.getLocationDao().getLocations(startDate, endDate)
    }

    fun deleteAllLocations() {
        locationDatabase.getLocationDao().deleteAllLocations()
    }

}