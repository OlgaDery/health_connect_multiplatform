package com.example.health_multiplstform

import com.example.health_multiplstform.database.HealthRecordsDatabaseProvider
import com.example.health_multiplstform.database.LocationRecordsDatabaseProvider
import com.example.health_multiplstform.healthRecordsDataSource.HealthClientProvider
import com.example.health_multiplstform.locationDataSource.ILocationDataSource
import com.example.health_multiplstform.locationDataSource.LocationCallbackManager
import com.example.health_multiplstform.locationDataSource.LocationServiceNativeClient
import com.example.health_multiplstform.repos.HealthRecordsRepository
import com.example.health_multiplstform.repos.LocationDataRepository
import com.example.health_multiplstform.shared.cache.AppDatabase
import com.example.health_multiplstform.utils.DataTypesProvider

expect class DependencyManager {

    val appDataBase: AppDatabase
    val healthRecordsDatabase: HealthRecordsDatabaseProvider
    val locationRecordsDatabase: LocationRecordsDatabaseProvider
    val dataTypesProvider: DataTypesProvider
    val healthClientProvider: HealthClientProvider
    val locationCallbackManager: LocationCallbackManager
    val locationDataSource: ILocationDataSource
    val healthRepo: HealthRecordsRepository
    val locationDataRepository: LocationDataRepository
    val locationClient: LocationServiceNativeClient
}

