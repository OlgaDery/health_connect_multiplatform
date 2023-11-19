package com.example.health_multiplstform

import com.example.health_multiplstform.FakeData.givenLocationRecord
import com.example.health_multiplstform.FakeData.givenHealthRecord
import com.example.health_multiplstform.FakeData.givenHealthRecord1
import com.example.health_multiplstform.FakeData.givenHealthRecordFromNovember10
import com.example.health_multiplstform.FakeData.givenMockRecordWithTimeMatchingHealthRecord1
import com.example.health_multiplstform.models.ConsolidatedRecord
import com.example.health_multiplstform.repos.IHealthRecordsRepository
import com.example.health_multiplstform.repos.ILocationDataRepository
import com.example.health_multiplstform.ui.RecordsListViewModel
import com.example.health_multiplstform.utils.IDataTypesProvider
import io.mockative.Mock
import io.mockative.classOf
import io.mockative.coVerify
import io.mockative.every
import io.mockative.mock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Month
import kotlin.test.Test
import kotlin.test.assertEquals

class HealthRecordsViewModelTest {
    @Mock
    val mockHealthRepo = mock(classOf<IHealthRecordsRepository>())
    @Mock
    val mockLocationRepo = mock(classOf<ILocationDataRepository>())
    @Mock
    val mockDataTypesProvider = mock(classOf<IDataTypesProvider>())

    @Test
    fun testValidateRecordToDisplayDoesNotHaveLocationIfTimeDoesNotMatch() {
        val sharedFlow = flowOf(listOf(givenHealthRecord)).shareIn(
            CoroutineScope(Dispatchers.Unconfined), started = SharingStarted.WhileSubscribed())

        every { mockLocationRepo.locationRecordsFlow }
            .returns(flowOf(listOf(givenLocationRecord)))

        every { mockHealthRepo.healthRecordsFlow}
            .invokes { Unit -> sharedFlow }

        runBlocking {

            val viewModel = RecordsListViewModel(mockHealthRepo, mockLocationRepo, mockDataTypesProvider)

            viewModel.recordsToDisplay.first().first().apply {
                assertEquals(givenHealthRecord.stepsMadeSinceLastRecord.toString(), this.steps)
                assertEquals(null, this.locationRecord)
            }
        }
    }

    @Test
    fun testValidateRecordToDisplayDoesHaveLocationIfTimeMatch() {
        val sharedFlow = flowOf(listOf(givenHealthRecord1)).shareIn(
            CoroutineScope(Dispatchers.Unconfined), started = SharingStarted.WhileSubscribed())

        every { mockLocationRepo.locationRecordsFlow }
            .returns(flowOf(listOf(givenMockRecordWithTimeMatchingHealthRecord1)))

        every { mockHealthRepo.healthRecordsFlow}
            .invokes { Unit -> sharedFlow }

        runBlocking {

            val viewModel = RecordsListViewModel(mockHealthRepo, mockLocationRepo, mockDataTypesProvider)

            viewModel.recordsToDisplay.first().first().apply {
                assertEquals(givenHealthRecord.stepsMadeSinceLastRecord.toString(), this.steps)
                assertEquals(givenMockRecordWithTimeMatchingHealthRecord1, this.locationRecord)
            }
        }
    }

    @Test
    fun testValidateRecordToDisplayDoesHaveLocationIfIdMatch() {
        val sharedFlow = flowOf(listOf(givenHealthRecord1)).shareIn(
            CoroutineScope(Dispatchers.Unconfined), started = SharingStarted.WhileSubscribed())

        every { mockLocationRepo.locationRecordsFlow }
            .returns(flowOf(listOf(givenMockRecordWithTimeMatchingHealthRecord1)))

        every { mockHealthRepo.healthRecordsFlow}
            .invokes { Unit -> sharedFlow }

        runBlocking {

            val viewModel = RecordsListViewModel(mockHealthRepo, mockLocationRepo, mockDataTypesProvider)

            viewModel.recordsToDisplay.first().first().apply {
                assertEquals(givenHealthRecord.stepsMadeSinceLastRecord.toString(), this.steps)
                assertEquals(givenMockRecordWithTimeMatchingHealthRecord1, this.locationRecord)
            }
        }
    }

    @Test
    fun testValidateDatesToDisplayValuePresentedInThereIsRecordForCurrentMonth() {
        val sharedFlowOfHealthRecords = flowOf(listOf(givenHealthRecordFromNovember10)).shareIn(
            CoroutineScope(Dispatchers.Unconfined), started = SharingStarted.WhileSubscribed())

        every { mockHealthRepo.healthRecordsFlow}
            .invokes { Unit -> sharedFlowOfHealthRecords }

        every { mockLocationRepo.locationRecordsFlow }
            .returns(flowOf(listOf(givenMockRecordWithTimeMatchingHealthRecord1)))

        every { mockDataTypesProvider.currentMonth() }.returns(Month.NOVEMBER)
        every { mockDataTypesProvider.monthNumber(1699649829917) }.returns(11)
        every { mockDataTypesProvider.dayOfMonth(1699649829917) }.returns(10)

        runBlocking {

            val viewModel = RecordsListViewModel(mockHealthRepo, mockLocationRepo, mockDataTypesProvider)
           assertEquals(10, viewModel.datesToDisplay.first().first())
        }
    }

    @Test
    fun testValidateDatesToDisplayIsEmptyIfNoDataForCurrentMonth() {
        val sharedFlowOfHealthRecords = flowOf(listOf<ConsolidatedRecord>()).shareIn(
            CoroutineScope(Dispatchers.Unconfined), started = SharingStarted.WhileSubscribed())

        every { mockHealthRepo.healthRecordsFlow}
            .invokes { Unit -> sharedFlowOfHealthRecords }

        every { mockLocationRepo.locationRecordsFlow }
            .returns(flowOf(listOf(givenMockRecordWithTimeMatchingHealthRecord1)))

        every { mockDataTypesProvider.currentMonth() }.returns(Month.NOVEMBER)
        every { mockDataTypesProvider.monthNumber(1699649829917) }.returns(11)
        every { mockDataTypesProvider.dayOfMonth(1699649829917) }.returns(10)

        runBlocking {

            val viewModel = RecordsListViewModel(mockHealthRepo, mockLocationRepo, mockDataTypesProvider)
            assertEquals(0, viewModel.datesToDisplay.first().size)
        }
    }

    @Test
    fun testValidateDeleteAllRecordsExpectedFunctionsCalled() {
        val sharedFlow = flowOf(listOf(givenHealthRecord1)).shareIn(
            CoroutineScope(Dispatchers.Unconfined), started = SharingStarted.WhileSubscribed())

        every { mockLocationRepo.locationRecordsFlow }
            .returns(flowOf(listOf(givenMockRecordWithTimeMatchingHealthRecord1)))

        every { mockHealthRepo.healthRecordsFlow}
            .invokes { Unit -> sharedFlow }

        val viewModel = RecordsListViewModel(mockHealthRepo, mockLocationRepo, mockDataTypesProvider)
        viewModel.coroutineContext = Dispatchers.Unconfined

        runBlocking {
            viewModel.deleteAllRecords()
            coVerify { mockHealthRepo.deleteAllHealthRecords() }
                .wasInvoked(atLeast = 1, atMost = 1)
            coVerify { mockLocationRepo.deleteAllLocationRecords() }
                .wasInvoked(atLeast = 1, atMost = 1)
        }
    }



}