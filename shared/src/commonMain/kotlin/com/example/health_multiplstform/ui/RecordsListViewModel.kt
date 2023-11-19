package com.example.health_multiplstform.ui

import com.example.health_multiplstform.repos.IHealthRecordsRepository
import com.example.health_multiplstform.models.ConsolidatedRecord
import com.example.health_multiplstform.models.HealthDataProvider
import com.example.health_multiplstform.models.LocationRecord
import com.example.health_multiplstform.repos.ILocationDataRepository
import com.example.health_multiplstform.utils.DataTypesProvider
import com.example.health_multiplstform.utils.IDataTypesProvider
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.Month
import kotlinx.datetime.number
import kotlin.coroutines.CoroutineContext

class RecordsListViewModel(private val healthRepo: IHealthRecordsRepository,
                           private val locationDataRepository: ILocationDataRepository,
                           private val dataTypesProvider: IDataTypesProvider

): ViewModel(), CoroutineScope {

    private val locationRecordThreshold = 1000*60*5L
    val currentMonth: Month
        get() {
            return dataTypesProvider.currentMonth()
        }

    private val _flow: Flow<List<RecordToDisplay>> =
        healthRepo.healthRecordsFlow.combine(locationDataRepository.locationRecordsFlow) { healthRec, locationRec ->
            healthRec.map { healthRecord ->
                val recordToMap = healthRecord.determineIfLocationMatchesHealthRecord(locationRec, locationRecordThreshold)
                RecordToDisplay.fromConsolidatedRecord(healthRecord, recordToMap)
        }
    }

    private val _dates: Flow<List<Int>> = healthRepo.healthRecordsFlow
        .map {
            it.filter { dataTypesProvider.monthNumber(it.time) == currentMonth.number}
                .map { dataTypesProvider.dayOfMonth(it.time) }
    }

    val recordsToDisplay = _flow
    val datesToDisplay = _dates

    fun deleteAllRecords() {
        launch {
            healthRepo.deleteAllHealthRecords()
            locationDataRepository.deleteAllLocationRecords()
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    override var coroutineContext: CoroutineContext = Dispatchers.Default
}

data class RecordToDisplay(
    val temperature: String,
    val steps: String,
    val heartBeat: String,
    val time: String,
    val healthDataProvider: HealthDataProvider,
    val locationRecord: LocationRecord?) {
    companion object {
        fun fromConsolidatedRecord(consolidatedRecord: ConsolidatedRecord,
                                   locationRecord: LocationRecord?): RecordToDisplay {
            val display = RecordToDisplay(
                if (consolidatedRecord.bodyTemperature > -1) consolidatedRecord.bodyTemperature.toString() else "No data found",
                consolidatedRecord.stepsMadeSinceLastRecord.toString(),
                if (consolidatedRecord.heartBeat > -1) consolidatedRecord.heartBeat.toString() else "No data found",
                DataTypesProvider().stringValue(consolidatedRecord.time),
                consolidatedRecord.healthDataProvider,
                locationRecord
            )
            return display
        }
    }
}