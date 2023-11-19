package com.example.health_multiplstform.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.health_multiplstform.models.ConsolidatedRecord
import com.example.health_multiplstform.models.HealthDataProvider
import com.example.health_multiplstform.shared.cache.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

interface IHealthRecordsDatabaseProvider {
    fun selectRecordsFromDelight(): Flow<List<ConsolidatedRecord>>
    suspend fun insertRecordToSqlDelight(record: ConsolidatedRecord)
    suspend fun deleteAllHealthRecords()
    suspend fun selectTimeOfMostRecentRecordFromHealthProvider(): Long?
}

class HealthRecordsDatabaseProvider(private val appDatabase: AppDatabase) : IHealthRecordsDatabaseProvider {

    override suspend fun insertRecordToSqlDelight(record: ConsolidatedRecord) {
        val dbQuery = appDatabase.appDatabaseQueries
        dbQuery.insertConsolidatedRecordEntity(
            id = record.id,
            bodyTemperature = record.bodyTemperature.toLong(),
            heartBeat = record.heartBeat,
            time = record.time,
            stepsMadeSinceLastRecord = record.stepsMadeSinceLastRecord,
            healthDataProvider = record.healthDataProvider.index.toLong())
    }

    override suspend fun deleteAllHealthRecords() {
        appDatabase.appDatabaseQueries.deleteAllHealthRecords()
    }

    override suspend fun selectTimeOfMostRecentRecordFromHealthProvider(): Long? {
        return appDatabase.appDatabaseQueries.selectMostRecentRecordFromHealthProvider(::mapDataClassToEntity).executeAsOneOrNull()?.time
    }

    override fun selectRecordsFromDelight(): Flow<List<ConsolidatedRecord>> {
        return appDatabase.appDatabaseQueries.selectAllConsolidatedRecordEntities(::mapDataClassToEntity)
            .asFlow().mapToList(Dispatchers.Default)
    }

    private fun mapDataClassToEntity(id: String, bodyTemperature: Long,
                                     heartBeat: Long, time: Long, stepsMadeSinceLastRecord: Long, healthDataProvider: Long): ConsolidatedRecord {

        return ConsolidatedRecord(
            id = id,
            bodyTemperature = bodyTemperature.toDouble(),
            heartBeat = heartBeat,
            time = time,
            stepsMadeSinceLastRecord = stepsMadeSinceLastRecord,
            healthDataProvider = HealthDataProvider.fromValue(healthDataProvider.toInt())
        )
    }

}