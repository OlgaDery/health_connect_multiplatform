package com.example.health_multiplstform.ui

import com.example.health_multiplstform.repos.IHealthRecordsRepository
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PagerViewModel(healthRepo: IHealthRecordsRepository): ViewModel() {

    private val _healthDataValue: Flow<HealthValues> = healthRepo
        .healthRecordsFlow
        .map { records ->
            if (records.isEmpty()) {
                HealthValues(
                    steps = 0,
                    heartRate = 0f,
                    total = 0
                )
            } else {
                HealthValues(
                    steps = records.sumOf { it.stepsMadeSinceLastRecord }.toInt(),
                    heartRate = (records.sumOf { it.heartBeat }/records.size).toFloat(),
                    total = records.size
                )
            }
    }

    val healthDataValue: Flow<HealthValues> = _healthDataValue

    override fun onCleared() {
        super.onCleared()
    }
}

data class HealthValues(val steps: Int, val heartRate: Float, val total: Int)