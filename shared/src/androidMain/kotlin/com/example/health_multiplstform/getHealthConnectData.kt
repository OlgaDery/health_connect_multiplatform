package com.example.health_multiplstform

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.BasalBodyTemperatureRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.time.Instant
import java.util.Date

internal actual suspend fun getHealthConnectStepsData(startTime: Long, endTime: Long, healthConnectClient: Any?): Int {
    if (healthConnectClient is HealthConnectClient) {
        try {
            var countForTimeRange = 0L
            healthConnectClient.readRecords(
                ReadRecordsRequest(
                    StepsRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(Instant.ofEpochMilli(startTime), Instant.ofEpochMilli(endTime))
                )
            ).let {
                println("found step records: " + it.records.size)
                if (it.records.isEmpty()) {
                    return -1
                }
                for (record in it.records) {
                    // Process each step record
                    countForTimeRange += record.count
                    println("found steps: " + record.count)
                }
                return countForTimeRange.toInt()
            }
        } catch (e: Exception) {
            // Run error handling here.
            return -1
        }
    } else {
        return -1
    }
}

internal actual suspend fun readPressureByTimeRange(
    startTime: Long,
    endTime: Long,
    healthConnectClient: Any?
): List<Pair<Double?, Double?>> {

    if (healthConnectClient is HealthConnectClient) {
        try {
            val response =
                healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        BloodPressureRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(Instant.ofEpochMilli(startTime), Instant.ofEpochMilli(endTime))
                    )
                )
            response.let { record ->
                record.records.forEach {
                    println("pressure record found: " + it.toString())
                }
                return record.records.map { Pair(it.diastolic.inMillimetersOfMercury, it.systolic.inMillimetersOfMercury)}
            }

        } catch (e: Exception) { }
    }
    return emptyList()
}

internal actual suspend fun readBasalTemperatureByTimeRange(
    startTime: Long,
    endTime: Long,
    healthConnectClient: Any?
): List<Double?> {
    if (healthConnectClient is HealthConnectClient) {
        try {
            val response =
                healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        BasalBodyTemperatureRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(Instant.ofEpochMilli(startTime), Instant.ofEpochMilli(endTime))
                    )
                )
            response.let {
                it.records.forEach {
                    println("body temp record found: " + it.temperature)
                }
                return it.records.map { it.temperature.inCelsius }
            }

        } catch (e: Exception) { }
    }
    return emptyList()
}

internal actual suspend fun readHeartRateByTimeRange(
    startTime: Long,
    endTime: Long,
    healthConnectClient: Any?
): List<Long?> {
    if (healthConnectClient is HealthConnectClient) {
        try {
            val response =
                healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        HeartRateRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(Instant.ofEpochMilli(startTime), Instant.ofEpochMilli(endTime))
                    )
                )
            response.let {
                val returnList = mutableListOf<Long?>()
                it.records.map { it.samples.map { it.beatsPerMinute } }.forEach {
                    it.forEach {
                        println("heart rate record found: " + it)
                    }
                    returnList.addAll(it)
                }
                return returnList
            }

        } catch (e: Exception) { }
    }
    return emptyList()
}