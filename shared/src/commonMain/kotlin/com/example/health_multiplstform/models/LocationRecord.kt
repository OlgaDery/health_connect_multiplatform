package com.example.health_multiplstform.models

data class LocationRecord (
    val longitude: Double,
    val latitude: Double,
    val timestamp: Long,
    val idOfSourceRecord: String?
)