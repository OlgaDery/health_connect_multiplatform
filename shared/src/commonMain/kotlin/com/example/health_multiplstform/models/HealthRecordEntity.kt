package com.example.health_multiplstform.models

import com.example.health_multiplstform.getPlatform
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ConsolidatedRecordEntity(
    @SerialName("id")
    val id: String = getPlatform().uuid,
    @SerialName("systolic")
    val systolic: Double,
    @SerialName("diastolic")
    val diastolic: Double,
    @SerialName("bodyTemperature")
    val bodyTemperature: Double,
    @SerialName("heartBeat")
    val heartBeat: Long,
    @SerialName("time")
    val time: Long,
    @SerialName("stepsMadeSinceLastRecord")
    val stepsMadeSinceLastRecord: Long,
    @SerialName("healthDataProvider")
    val healthDataProvider: Long
)