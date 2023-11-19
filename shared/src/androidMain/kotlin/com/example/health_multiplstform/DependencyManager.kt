package com.example.health_multiplstform

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import com.example.health_multiplstform.database.DatabaseDriverFactory
import com.example.health_multiplstform.database.HealthRecordsDatabaseProvider
import com.example.health_multiplstform.database.LocationRecordsDatabaseProvider
import com.example.health_multiplstform.healthRecordsDataSource.HealthClientProvider
import com.example.health_multiplstform.locationDataSource.ILocationDataSource
import com.example.health_multiplstform.locationDataSource.LocationCallbackManager
import com.example.health_multiplstform.locationDataSource.LocationDataSource
import com.example.health_multiplstform.locationDataSource.LocationServiceNativeClient
import com.example.health_multiplstform.repos.HealthRecordsRepository
import com.example.health_multiplstform.repos.LocationDataRepository
import com.example.health_multiplstform.shared.cache.AppDatabase
import com.example.health_multiplstform.utils.DataTypesProvider
import java.util.Date

actual class DependencyManager (context: Context,  val healthConnectClient: HealthConnectClient?) {

    actual val appDataBase: AppDatabase
    actual val healthRecordsDatabase: HealthRecordsDatabaseProvider
    actual val locationRecordsDatabase: LocationRecordsDatabaseProvider
    actual val dataTypesProvider: DataTypesProvider
    actual val healthClientProvider: HealthClientProvider
    actual val locationCallbackManager: LocationCallbackManager
    actual val locationDataSource: ILocationDataSource
    actual val healthRepo: HealthRecordsRepository
    actual val locationDataRepository: LocationDataRepository
    actual val locationClient: LocationServiceNativeClient

    init {
        appDataBase = DatabaseDriverFactory().createDriver(context)?.let{
            AppDatabase(it)
        }!!
        dataTypesProvider = DataTypesProvider()

        //health classes
        healthRecordsDatabase = HealthRecordsDatabaseProvider(appDataBase)
        healthClientProvider = HealthClientProvider(healthConnectClient)
        healthRepo = HealthRecordsRepository(healthRecordsDatabase, dataTypesProvider, healthClientProvider)

        //location classes
        locationRecordsDatabase = LocationRecordsDatabaseProvider(appDataBase)
        locationClient = LocationServiceNativeClient(context)
        locationDataSource = LocationDataSource(locationClient)
        locationCallbackManager = LocationCallbackManager(locationDataSource)
        locationDataRepository = LocationDataRepository(locationRecordsDatabase, locationCallbackManager)
    }

}