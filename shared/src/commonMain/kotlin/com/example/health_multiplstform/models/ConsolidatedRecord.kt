package com.example.health_multiplstform.models

import com.example.health_multiplstform.getPlatform
import kotlin.math.abs

data class ConsolidatedRecord(
    val id: String = getPlatform().uuid,
    val bodyTemperature: Double,
    val heartBeat: Long,
    val time: Long,
    val stepsMadeSinceLastRecord: Long,
    val healthDataProvider: HealthDataProvider = HealthDataProvider.PlatformHealthProvider
) {
    fun determineIfLocationMatchesHealthRecord(records: List<LocationRecord>, locationRecordThreshold: Long): LocationRecord? {
        return records.firstOrNull{
            it.idOfSourceRecord == this.id
                    || (abs(it.timestamp - this.time) < locationRecordThreshold)
        }
    }
}

enum class HealthDataProvider(val index: Int) {
    PlatformHealthProvider(0), UserInput(1), Unknown(2);

    companion object {
        fun fromValue(value: Int): HealthDataProvider = when (value) {
            0 -> PlatformHealthProvider
            1 -> UserInput
            else -> Unknown
        }
    }
}
