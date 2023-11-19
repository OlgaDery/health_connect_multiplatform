package com.example.health_multiplstform

import com.example.health_multiplstform.database.IHealthRecordsDatabaseProvider
import com.example.health_multiplstform.healthRecordsDataSource.IHealthClientProvider
import com.example.health_multiplstform.models.ConsolidatedRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeDatabaseWithEmptyFlow: IHealthRecordsDatabaseProvider {
    override fun selectRecordsFromDelight(): Flow<List<ConsolidatedRecord>> {
        return flowOf(listOf())
    }

    override suspend fun insertRecordToSqlDelight(record: ConsolidatedRecord) {}

    override suspend fun deleteAllHealthRecords() {}

    override suspend fun selectTimeOfMostRecentRecordFromHealthProvider(): Long? {
        return null
    }
}

class FakeDatabaseWithTwoRecordsInFlow: IHealthRecordsDatabaseProvider {
    override fun selectRecordsFromDelight(): Flow<List<ConsolidatedRecord>> {
        return flowOf(listOf(FakeData.givenHealthRecord, FakeData.givenHealthRecord1) )
    }

    override suspend fun insertRecordToSqlDelight(record: ConsolidatedRecord) {
    }

    override suspend fun deleteAllHealthRecords() {
    }

    override suspend fun selectTimeOfMostRecentRecordFromHealthProvider(): Long? {
        return null
    }
}

class FakeHealthProviderFullData(val initialHealthData: InitialHealthData = InitialHealthData()): IHealthClientProvider {
    override suspend fun readStepsData(startTime: Long, endTime: Long): Int {
        return initialHealthData.steps
    }

    override suspend fun readPressure(
        startTime: Long,
        endTime: Long
    ): List<Pair<Double?, Double?>> {
        return initialHealthData.pressure
    }

    override suspend fun readBasalTemperature(
        startTime: Long,
        endTime: Long
    ): List<Double?> {
        return initialHealthData.basalTemp
    }

    override suspend fun readHeartRate(
        startTime: Long,
        endTime: Long
    ): List<Long?> {
        return initialHealthData.heartRate
    }
}

data class InitialHealthData(
    val pressure: List<Pair<Double?, Double?>> = listOf(),
    val basalTemp: List<Double?> = listOf(),
    val heartRate: List<Long?> = listOf(),
    val steps: Int = -1)