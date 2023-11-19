package com.example.health_multiplstform.repos

import com.example.health_multiplstform.database.ILocationRecordsDatabaseProvider
import com.example.health_multiplstform.locationDataSource.ILocationCallbackManager
import com.example.health_multiplstform.models.LocationRecord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

interface ILocationDataRepository {
    val locationRecordsFlow:  Flow<List<LocationRecord>>
    fun startDataCollectionJob()
    fun stopDataCollectionJob()
    suspend fun insertRecord(locationRecord: LocationRecord)
    suspend fun deleteAllLocationRecords()
}

class LocationDataRepository constructor(
    private val delightDb: ILocationRecordsDatabaseProvider,
    private val locationDataManager: ILocationCallbackManager,
    override var coroutineContext: CoroutineContext = Dispatchers.Default

): ILocationDataRepository, CoroutineScope {

    private val mutex = Mutex()

    @OptIn(ExperimentalCoroutinesApi::class)
    private var locationDataCollectionJob: Job = launch(start = CoroutineStart.LAZY) {

        println("should start records collection")
        locationDataManager.locationFlow().collect{
            insertRecord(it)
        }
    }

    override val locationRecordsFlow: Flow<List<LocationRecord>> = delightDb.selectLocationRecordsFromDelight()

    override fun startDataCollectionJob() {
        //"isActive" is not volatile, so it can return the wrong value if called from a different thread.
        //That's why we need to lock this part.
        launch {
            mutex.withLock {
                if (locationDataCollectionJob.isActive) {
                    println("location data job is active: " + locationDataCollectionJob.isActive)
                    return@launch
                }
                println("starting location data collection job!")
                locationDataCollectionJob.start()
            }
        }
    }

    override fun stopDataCollectionJob() {
        println("stop location stopDataCollectionJob!")
        if (locationDataCollectionJob.isActive) {
            locationDataCollectionJob.cancel()
        }
    }

    override suspend fun insertRecord(locationRecord: LocationRecord) {
        delightDb.insertRecordToSqlDelight(locationRecord)
    }

    override suspend fun deleteAllLocationRecords() {
        delightDb.deleteAllLocationRecords()
    }
}