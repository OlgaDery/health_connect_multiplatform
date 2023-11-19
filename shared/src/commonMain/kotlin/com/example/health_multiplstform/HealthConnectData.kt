package com.example.health_multiplstform

internal expect suspend fun getHealthConnectStepsData(startTime: Long, endTime: Long, healthConnectClient: Any?): Int

internal expect suspend fun readPressureByTimeRange(startTime: Long, endTime: Long, healthConnectClient: Any?): List<Pair<Double?, Double?>>

internal expect suspend fun readBasalTemperatureByTimeRange (startTime: Long, endTime: Long, healthConnectClient: Any?): List<Double?>

internal expect suspend fun readHeartRateByTimeRange (startTime: Long, endTime: Long, healthConnectClient: Any?): List<Long?>