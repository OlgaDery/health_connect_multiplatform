package com.example.health_multiplstform

import com.example.health_multiplstform.repos.HealthRecordsRepository
import com.example.health_multiplstform.utils.DataTypesProvider
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class HealthRecordsRepoTests {
    @Test
    fun testValidateSharedRecordsContainsNoValuesWhenNoRecordEmittedByDataSource() {
        val healthRecordsRepo = HealthRecordsRepository(
            delightDb = FakeDatabaseWithEmptyFlow(),
            dataTypesProvider = DataTypesProvider(),
            healthConnectProvider = FakeHealthProviderFullData()
        )
        runBlocking {
            assertEquals(0, healthRecordsRepo.healthRecordsFlow.first().size)
        }
    }

    @Test
    fun testValidateSharedRecordsContainsTwoValuesWhenTwoRecordEmittedByDataSource() {
        val healthRecordsRepo = HealthRecordsRepository(
            delightDb = FakeDatabaseWithTwoRecordsInFlow(),
            dataTypesProvider = DataTypesProvider(),
            healthConnectProvider = FakeHealthProviderFullData()
        )
        runBlocking {
             assertEquals(2, healthRecordsRepo.healthRecordsFlow.first().size)
        }
    }

    @Test
    fun testReadHealthDataFromHealthProviderAndConstructRecordAllDataPresented() {
        val data = InitialHealthData(
            pressure = listOf(Pair(55.0, 66.0)),
            basalTemp = listOf(37.5, 36.6, 36.5),
            heartRate = listOf(77L, 80L),
            steps = 10)

        val healthRecordsRepo = HealthRecordsRepository(
            delightDb = FakeDatabaseWithTwoRecordsInFlow(),
            dataTypesProvider = DataTypesProvider(),
            healthConnectProvider = FakeHealthProviderFullData(data)
        )
        runBlocking {
            val record = healthRecordsRepo.readHealthDataFromHealthProviderAndConstructRecord()
            assertEquals(expected = 10, record?.stepsMadeSinceLastRecord)
            assertEquals(expected = 36.5, record?.bodyTemperature)
            assertEquals(expected = 80L, record?.heartBeat)
        }
    }

    @Test
    fun testReadHealthDataFromHealthProviderAndConstructRecordNullReturnedForNoSteps() {

        val healthRecordsRepo = HealthRecordsRepository(
            delightDb = FakeDatabaseWithTwoRecordsInFlow(),
            dataTypesProvider = DataTypesProvider(),
            healthConnectProvider = FakeHealthProviderFullData()
        )
        runBlocking {
            val record = healthRecordsRepo.readHealthDataFromHealthProviderAndConstructRecord()
            assertNull(record)
        }
    }




}