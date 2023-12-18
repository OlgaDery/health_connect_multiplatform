package com.example.health_multiplstform

import com.example.health_multiplstform.utils.DataTypesProvider
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import platform.Foundation.NSDate
import platform.Foundation.NSDateComponents
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.timeIntervalSinceReferenceDate
import platform.HealthKit.HKHealthStore
import platform.HealthKit.HKQuantityType
import platform.HealthKit.HKQuantityTypeIdentifierStepCount
import platform.HealthKit.HKQuery
import platform.HealthKit.HKQueryOptionStrictEndDate
import platform.HealthKit.HKStatisticsCollection
import platform.HealthKit.HKStatisticsCollectionQuery
import platform.HealthKit.HKStatisticsOptionCumulativeSum
import platform.HealthKit.HKUnit
import platform.HealthKit.countUnit
import platform.HealthKit.predicateForSamplesWithStartDate
import platform.posix.time

@OptIn(ExperimentalForeignApi::class, ExperimentalCoroutinesApi::class)
actual suspend fun getHealthConnectStepsData(
    startTime: Long,
    endTime: Long,
    healthConnectClient: Any?

): Int = suspendCancellableCoroutine { continuation ->
    if (healthConnectClient is HKHealthStore) {
        val startDate = NSDate.dateWithTimeIntervalSince1970(startTime.toDouble()/1000)
        val endDate = NSDate.dateWithTimeIntervalSince1970(endTime.toDouble()/1000)

        val dateComponents = NSDateComponents()
        dateComponents.hour = 0
        dateComponents.minute = 3
        var value = 0
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
            if (result?.statistics().isNullOrEmpty()) {
                continuation.resume(-1, {})
            } else {
                result?.enumerateStatisticsFromDate(
                    startDate = startDate,
                    toDate = endDate,
                    withBlock = { statistics, _ ->
                        run {
                            statistics?.sumQuantity()?.doubleValueForUnit(unit = HKUnit.countUnit())
                                ?.toInt()?.let {
                                    value += it
                                }

                        }
                    }
                )
                continuation.resume(value, {})
            }
        }
        healthConnectClient.executeQuery(queryStepCount)
    }
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