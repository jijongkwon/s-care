package com.scare.data.walk.dto

data class WalkRequestDTO(
    val startedAt: String,
    val finishedAt: String,
    val heartRates: List<Double>,
    val locations: List<LocationDTO>,
)

data class LocationDTO(
    val latitude: Double,
    val longitude: Double
)