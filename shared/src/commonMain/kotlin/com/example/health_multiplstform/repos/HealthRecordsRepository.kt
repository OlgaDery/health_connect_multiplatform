package com.example.health_multiplstform.repos

import com.example.health_multiplstform.database.IHealthRecordsDatabaseProvider
import com.example.health_multiplstform.healthRecordsDataSource.IHealthClientProvider
import com.example.health_multiplstform.models.ConsolidatedRecord
import com.example.health_multiplstform.models.HealthDataProvider
import com.example.health_multiplstform.utils.DataTypesProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.CoroutineContext

interface IHealthRecordsRepository {
    val healthRecordsFlow: SharedFlow<List<ConsolidatedRecord>>

    var checkingRecordsSince: Long
    fun launchDataCollectionJob()
    fun stopDataCollectionJob()
    suspend fun readHealthDataFromHealthProviderAndConstructRecord(): ConsolidatedRecord?
    suspend fun insertHealthRecord(record: ConsolidatedRecord)
    suspend fun deleteAllHealthRecords()
}

class HealthRecordsRepository constructor(
    private val delightDb: IHealthRecordsDatabaseProvider,
    private val dataTypesProvider: DataTypesProvider,
    private val healthConnectProvider: IHealthClientProvider,
    override var coroutineContext: CoroutineContext = Dispatchers.Default

): CoroutineScope, IHealthRecordsRepository {

    private var dataCheckJob: Job? = launch(start = CoroutineStart.LAZY) {
        while (this.isActive) {
            println("looking for new health records")
            val record = readHealthDataFromHealthProviderAndConstructRecord()
            if (record != null) {
                insertHealthRecord(record = record)
                checkingRecordsSince = record.time
            }
            delay(dataCollectionJobDelay)
        }
    }

    var dataCollectionJobDelay = 1000*60*3L
    private val mutex = Mutex()

    override val healthRecordsFlow: SharedFlow<List<ConsolidatedRecord>> =
        delightDb.selectRecordsFromDelight()
            .shareIn(
                scope = CoroutineScope(coroutineContext),
                started = SharingStarted.WhileSubscribed(5000),
                replay = 1
            )

    override var checkingRecordsSince = dataTypesProvider.now

    init {
        launch {
            delightDb.selectTimeOfMostRecentRecordFromHealthProvider()?.let {
                checkingRecordsSince = it
            }
        }
    }

    override fun launchDataCollectionJob() {
        launch {
            mutex.withLock {
                if (dataCheckJob?.isActive == true) {
                    return@launch
                }
                dataCheckJob?.start()
            }
        }
    }

    override fun stopDataCollectionJob() {
        dataCheckJob?.cancel()
    }

    override suspend fun readHealthDataFromHealthProviderAndConstructRecord(): ConsolidatedRecord? {

        val newSteps = healthConnectProvider.readStepsData(
            startTime = checkingRecordsSince,
            endTime = dataTypesProvider.now)

        val heartRate = healthConnectProvider.readHeartRate(
            startTime = checkingRecordsSince,
            endTime = dataTypesProvider.now)
            .lastOrNull()

        val temp = healthConnectProvider.readBasalTemperature(
            startTime = checkingRecordsSince,
            endTime = dataTypesProvider.now)
            .lastOrNull()

        if (newSteps <= -1) {
            return null
        }
        return ConsolidatedRecord(
            bodyTemperature = temp ?: -1.0,
            heartBeat = heartRate ?: -1L,
            time = dataTypesProvider.now,
            stepsMadeSinceLastRecord = newSteps.toLong(),
            healthDataProvider = HealthDataProvider.PlatformHealthProvider)
    }

    override suspend fun insertHealthRecord(record: ConsolidatedRecord) {
        delightDb.insertRecordToSqlDelight(record)

    }

    override suspend fun deleteAllHealthRecords() {
        delightDb.deleteAllHealthRecords()
    }

}