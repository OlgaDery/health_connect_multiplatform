package com.example.health_multiplstform

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDate
import platform.Foundation.NSDateComponents
import platform.HealthKit.HKCategoryTypeIdentifier
import platform.HealthKit.HKHealthStore
import platform.HealthKit.HKQuantityType
import platform.HealthKit.HKQuantityTypeIdentifierStepCount
import platform.HealthKit.HKQuery
import platform.HealthKit.HKQueryOptionStrictEndDate
import platform.HealthKit.HKStatisticsCollectionQuery
import platform.HealthKit.HKStatisticsOptionCumulativeSum
import platform.HealthKit.HKUnit
import platform.HealthKit.countUnit
import platform.HealthKit.predicateForSamplesWithStartDate

@OptIn(ExperimentalForeignApi::class)
actual suspend fun getHealthConnectStepsData(startTime: Long, endTime: Long, healthConnectClient: Any?): Int {
    return -1
}

actual suspend fun readPressureByTimeRange(
    startTime: Long,
    endTime: Long,
    healthConnectClient: Any?
): List<Pair<Double?, Double?>> {
    TODO("Not yet implemented")
}

actual suspend fun readBasalTemperatureByTimeRange(
    startTime: Long,
    endTime: Long,
    healthConnectClient: Any?
): List<Double?> {
    TODO("Not yet implemented")
}

actual suspend fun readHeartRateByTimeRange(
    startTime: Long,
    endTime: Long,
    healthConnectClient: Any?
): List<Long?> {
    TODO("Not yet implemented")
}