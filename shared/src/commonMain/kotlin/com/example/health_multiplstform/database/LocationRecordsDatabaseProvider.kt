package com.example.health_multiplstform.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.health_multiplstform.getPlatform
import com.example.health_multiplstform.models.ConsolidatedRecord
import com.example.health_multiplstform.models.HealthDataProvider
import com.example.health_multiplstform.models.LocationRecord
import com.example.health_multiplstform.shared.cache.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

interface ILocationRecordsDatabaseProvider {
    fun selectLocationRecordsFromDelight(): Flow<List<LocationRecord>>
    suspend fun insertRecordToSqlDelight(record: LocationRecord)
    suspend fun deleteAllLocationRecords()
}

class LocationRecordsDatabaseProvider(private val appDatabase: AppDatabase) : ILocationRecordsDatabaseProvider {
    override suspend fun insertRecordToSqlDelight(record: LocationRecord) {
        val dbQuery = appDatabase.appDatabaseQueries
        dbQuery.insertCoordinates(
            id = getPlatform().uuid,
            longitude = record.longitude,
            latitude = record.latitude,
            timestamp = record.timestamp,
            idOfSource = record.idOfSourceRecord)
    }

    override suspend fun deleteAllLocationRecords() {
        appDatabase.appDatabaseQueries.deleteAllCoordinates()
    }

    override fun selectLocationRecordsFromDelight(): Flow<List<LocationRecord>> {
        return appDatabase.appDatabaseQueries.selectAllCoordinates(::mapEntityToLocationClass)
            .asFlow().mapToList(Dispatchers.Default)
    }

    private fun mapEntityToLocationClass(id: String, longitude:Double,
                                         latitude: Double, time: Long, idOfSource: String?): LocationRecord {
        return LocationRecord(
            longitude = longitude,
            latitude = latitude,
            timestamp = time,
            idOfSourceRecord = idOfSource
        )
    }

}