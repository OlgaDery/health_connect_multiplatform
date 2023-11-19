package com.example.health_multiplstform

import com.example.health_multiplstform.models.ConsolidatedRecord
import com.example.health_multiplstform.repos.IHealthRecordsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.shareIn

class FakeHealthRepoWithOneRecord: IHealthRecordsRepository {
    override val healthRecordsFlow: SharedFlow<List<ConsolidatedRecord>>
        get() = flowOf(listOf(FakeData.givenHealthRecord))
            .shareIn(
                scope = CoroutineScope(Dispatchers.Unconfined) ,
                started = SharingStarted.WhileSubscribed()
            )
    override var checkingRecordsSince: Long = 0L
    override fun launchDataCollectionJob() {}
    override fun stopDataCollectionJob() {}
    override suspend fun readHealthDataFromHealthProviderAndConstructRecord(): ConsolidatedRecord? {
        return null
    }
    override suspend fun insertHealthRecord(record: ConsolidatedRecord) {}
    override suspend fun deleteAllHealthRecords() {}
}

class FakeHealthRepoWithNoRecord: IHealthRecordsRepository {
    override val healthRecordsFlow: SharedFlow<List<ConsolidatedRecord>>
        get() = flowOf(listOf<ConsolidatedRecord>())
            .shareIn(
                scope = CoroutineScope(Dispatchers.Unconfined) ,
                started = SharingStarted.WhileSubscribed()
            )

    override var checkingRecordsSince: Long = 0L
    override fun launchDataCollectionJob() {}
    override fun stopDataCollectionJob() {}
    override suspend fun readHealthDataFromHealthProviderAndConstructRecord(): ConsolidatedRecord? {
        return null
    }
    override suspend fun insertHealthRecord(record: ConsolidatedRecord) {}
    override suspend fun deleteAllHealthRecords() {}
}