package com.example.health_multiplstform.healthRecordsDataSource

import com.example.health_multiplstform.getHealthConnectStepsData
import com.example.health_multiplstform.readBasalTemperatureByTimeRange
import com.example.health_multiplstform.readHeartRateByTimeRange
import com.example.health_multiplstform.readPressureByTimeRange

interface IHealthClientProvider {
    suspend fun readStepsData(startTime: Long, endTime: Long): Int
    suspend fun readPressure(startTime: Long, endTime: Long): List<Pair<Double?, Double?>>
    suspend fun readBasalTemperature (startTime: Long, endTime: Long): List<Double?>
    suspend fun readHeartRate (startTime: Long, endTime: Long): List<Long?>
}

class HealthClientProvider(val nativeHealthConnectProvider: Any?): IHealthClientProvider {
    override suspend fun readStepsData(startTime: Long, endTime: Long): Int {
        return getHealthConnectStepsData(startTime = startTime, endTime = endTime, healthConnectClient = nativeHealthConnectProvider)
    }

    override suspend fun readPressure(
        startTime: Long,
        endTime: Long
    ): List<Pair<Double?, Double?>> {
        return readPressureByTimeRange(startTime = startTime, endTime = endTime, healthConnectClient = nativeHealthConnectProvider)
    }

    override suspend fun readBasalTemperature(
        startTime: Long,
        endTime: Long
    ): List<Double?> {
        return readBasalTemperatureByTimeRange(startTime = startTime, endTime = endTime, healthConnectClient = nativeHealthConnectProvider)
    }

    override suspend fun readHeartRate(
        startTime: Long,
        endTime: Long
    ): List<Long?> {
        return readHeartRateByTimeRange(startTime = startTime, endTime = endTime, healthConnectClient = nativeHealthConnectProvider)
    }

}

