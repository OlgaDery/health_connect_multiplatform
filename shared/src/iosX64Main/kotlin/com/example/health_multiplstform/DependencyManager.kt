package com.example.health_multiplstform

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
import platform.CoreLocation.CLLocationManager
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.NSTimeZone
import platform.Foundation.autoupdatingCurrentLocale
import platform.Foundation.localTimeZone
import platform.HealthKit.HKHealthStore

actual class DependencyManager constructor(locationNativeClient: CLLocationManager,
                                           healthStore: HKHealthStore) { //: ObservableObject
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
        appDataBase = DatabaseDriverFactory().createDriver(null)?.let{
            AppDatabase(it)
        }!!
        dataTypesProvider = DataTypesProvider()
        //health classes
        healthRecordsDatabase = HealthRecordsDatabaseProvider(appDataBase)
        healthClientProvider = HealthClientProvider(healthStore)
        healthRepo = HealthRecordsRepository(healthRecordsDatabase, dataTypesProvider, healthClientProvider)
        //location classes
        locationRecordsDatabase = LocationRecordsDatabaseProvider(appDataBase)
        locationClient = LocationServiceNativeClient(locationNativeClient)
        locationDataSource = LocationDataSource(locationClient)
        locationCallbackManager = LocationCallbackManager(locationDataSource)
        locationDataRepository = LocationDataRepository(locationRecordsDatabase, locationCallbackManager)
        locationClient.dataCollectionJobStartCallback = {locationDataRepository.startDataCollectionJob()}
        locationClient.dataCollectionJobCancellationCallback = {locationDataRepository.stopDataCollectionJob()}
    }
}