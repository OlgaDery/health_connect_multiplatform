package com.example.health_multiplstform.models
import com.example.health_multiplstform.getPlatform
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class LocationRecordEntity(
    @SerialName("id")
    val id: String = getPlatform().uuid,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("timestamp")
    val timestamp: Long,
    @SerialName("idOfSource")
    val idOfSource: String?
)