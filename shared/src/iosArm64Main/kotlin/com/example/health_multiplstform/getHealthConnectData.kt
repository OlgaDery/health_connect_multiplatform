package com.example.health_multiplstform

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDate
import platform.Foundation.NSDateComponents
import platform.Foundation.NSTimeInterval
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
    if (healthConnectClient is HKHealthStore) {
        val startDate = NSDate(timeIntervalSinceReferenceDate = startTime.toDouble())
        val endDate = NSDate(timeIntervalSinceReferenceDate = endTime.toDouble())


        val dateComponents = NSDateComponents()
        dateComponents.minute = 1
        var value = -1
        val type = HKQuantityType.quantityTypeForIdentifier(identifier = HKQuantityTypeIdentifierStepCount)
        val predicate = HKQuery.predicateForSamplesWithStartDate(
            startDate = startDate,
            endDate = endDate,
            options = HKQueryOptionStrictEndDate
        )
        val queryStepCount = HKStatisticsCollectionQuery(
            type!!,
            predicate,
            options = HKStatisticsOptionCumulativeSum,
            anchorDate = startDate,
            intervalComponents = dateComponents
        )
        queryStepCount.initialResultsHandler = {q, result, error ->
            result?.enumerateStatisticsFromDate(
                startDate = startDate,
                toDate = endDate,
                withBlock = {
                        statistics, _ ->
                    run {
                        value =
                            statistics?.sumQuantity()?.doubleValueForUnit(unit = HKUnit.countUnit())?.toInt() ?: -1
                        println("value found! " + value)
                    }
                })
        }

        healthConnectClient.executeQuery(queryStepCount)
        return value
    }
    return -1
}

actual suspend fun readPressureByTimeRange(
    startTime: Long,
    endTime: Long,
    healthConnectClient: Any?
): List<Pair<Double?, Double?>> {
    return emptyList()
}

actual suspend fun readBasalTemperatureByTimeRange(
    startTime: Long,
    endTime: Long,
    healthConnectClient: Any?
): List<Double?> {
    return emptyList()
}

actual suspend fun readHeartRateByTimeRange(
    startTime: Long,
    endTime: Long,
    healthConnectClient: Any?
): List<Long?> {
    return emptyList()
}