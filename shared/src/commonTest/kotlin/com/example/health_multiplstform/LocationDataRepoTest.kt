package com.example.health_multiplstform

import com.example.health_multiplstform.FakeData.givenLocationRecord
import com.example.health_multiplstform.FakeData.givenMockRecordWithTimeMatchingHealthRecord1
import com.example.health_multiplstform.database.ILocationRecordsDatabaseProvider
import com.example.health_multiplstform.locationDataSource.ILocationCallbackManager
import com.example.health_multiplstform.repos.LocationDataRepository
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.coVerify
import io.mockative.every
import io.mockative.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LocationDataRepoTest {
    @Mock
    val mockLocationCallbackManager = mock(classOf<ILocationCallbackManager>())
    @Mock
    val mockDatabase = mock(classOf<ILocationRecordsDatabaseProvider>())

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testValidateLocationRecordEmittedAndInsertedToDatabaseIfWorkerStarted() {

        every { mockLocationCallbackManager.locationFlow() }
            .returns(flowOf(givenLocationRecord) )

        every { mockDatabase.selectLocationRecordsFromDelight()}.returns(flowOf(listOf(givenLocationRecord, givenMockRecordWithTimeMatchingHealthRecord1)))

        val locationDataRepo = LocationDataRepository(
            mockDatabase,  mockLocationCallbackManager, Dispatchers.Unconfined
        )
        runBlocking {
            locationDataRepo.startDataCollectionJob()

            coVerify { mockLocationCallbackManager.locationFlow().collect() }
                .wasInvoked(atLeast = 1, atMost = 1)

            coVerify { mockDatabase.insertRecordToSqlDelight(givenLocationRecord) }
                .wasInvoked(atLeast = 1, atMost = 1)
        }
    }

    @Test
    fun testValidateLocationRecordCollectedFromDatabase() {
        every { mockDatabase.selectLocationRecordsFromDelight()}.returns(flowOf(listOf(givenLocationRecord, givenMockRecordWithTimeMatchingHealthRecord1)))

        val locationDataRepo = LocationDataRepository(
            mockDatabase,  mockLocationCallbackManager, Dispatchers.Unconfined
        )
        runBlocking {
            assertEquals(2, locationDataRepo.locationRecordsFlow.first().size)
        }
    }

}