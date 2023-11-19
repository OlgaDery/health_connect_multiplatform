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

actual class DependencyManager {
    actual val appDataBase: AppDatabase
        get() = TODO("Not yet implemented")
    actual val healthRecordsDatabase: HealthRecordsDatabaseProvider
        get() = TODO("Not yet implemented")
    actual val locationRecordsDatabase: LocationRecordsDatabaseProvider
        get() = TODO("Not yet implemented")
    actual val dataTypesProvider: DataTypesProvider
        get() = TODO("Not yet implemented")
    actual val healthClientProvider: HealthClientProvider
        get() = TODO("Not yet implemented")
    actual val locationCallbackManager: LocationCallbackManager
        get() = TODO("Not yet implemented")
    actual val locationDataSource: ILocationDataSource
        get() = TODO("Not yet implemented")
    actual val healthRepo: HealthRecordsRepository
        get() = TODO("Not yet implemented")
    actual val locationDataRepository: LocationDataRepository
        get() = TODO("Not yet implemented")
    actual val locationClient: LocationServiceNativeClient
        get() = TODO("Not yet implemented")

}