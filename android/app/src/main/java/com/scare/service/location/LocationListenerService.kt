package com.scare.service.location

import com.scare.repository.location.LocationRepository
import javax.inject.Inject

class LocationListenerService {

    @Inject
    lateinit var locationRepository: LocationRepository

}