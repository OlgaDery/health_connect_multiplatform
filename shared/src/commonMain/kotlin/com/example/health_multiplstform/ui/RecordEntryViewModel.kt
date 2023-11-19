package com.example.health_multiplstform.ui

import com.example.health_multiplstform.models.ConsolidatedRecord
import com.example.health_multiplstform.models.HealthDataProvider
import com.example.health_multiplstform.models.LocationRecord
import com.example.health_multiplstform.repos.IHealthRecordsRepository
import com.example.health_multiplstform.repos.ILocationDataRepository
import com.example.health_multiplstform.utils.DataTypesProvider
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class RecordEntryViewModel(
    private val healthRepo: IHealthRecordsRepository,
    private val locationDataRepository: ILocationDataRepository
): ViewModel(), CoroutineScope {

    var inputLocation: LocationRecord? = null

    fun constructAndInsertRecord(numberOfSteps: Int, heartRate: Int, bodyTemp: Int) {
        launch {
            val consolidatedRecord = ConsolidatedRecord(
                stepsMadeSinceLastRecord = numberOfSteps.toLong(),
                bodyTemperature = bodyTemp.toDouble(),
                heartBeat = heartRate.toLong(),
                time = DataTypesProvider().now,
                healthDataProvider = HealthDataProvider.UserInput
            )

            healthRepo.insertHealthRecord(consolidatedRecord)
            inputLocation?.let {
                val locationRecord = LocationRecord(
                    longitude = it.longitude,
                    latitude= it.latitude,
                    timestamp = consolidatedRecord.time,
                    idOfSourceRecord = consolidatedRecord.id)

                locationDataRepository.insertRecord(locationRecord)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

}