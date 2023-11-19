package com.example.health_multiplstform

import com.example.health_multiplstform.ui.PagerViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class CommonPageViewModelTest {

    @Test
    fun testValidateHealthDataValueContainsCorrectValuesWhenOneRecordEmitted() {
        val viewModel = PagerViewModel(FakeHealthRepoWithOneRecord())
        runBlocking {
            viewModel.healthDataValue.first().apply {
                assertEquals(this.steps, FakeData.givenHealthRecord.stepsMadeSinceLastRecord.toInt())
                assertEquals(this.heartRate, 11F)
                assertEquals(this.total, 1)
            }
        }
    }

    @Test
    fun testValidateHealthDataValueContainsCorrectValuesWhenNoRecordEmitted() {
        val viewModel = PagerViewModel(FakeHealthRepoWithNoRecord())
        runBlocking {
            viewModel.healthDataValue.first().apply {
                assertEquals(this.steps, 0)
                assertEquals(this.heartRate, 0F)
                assertEquals(this.total, 0)
            }
        }
    }
}