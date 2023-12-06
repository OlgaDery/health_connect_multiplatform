package com.example.health_multiplstform.locationDataSource

import com.example.health_multiplstform.models.LocationRecord
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface ILocationCallbackManager {
    @ExperimentalCoroutinesApi
    fun locationFlow(): Flow<LocationRecord>
}

class LocationCallbackManager(
    private val locationDatasource: ILocationDataSource

) : ILocationCallbackManager {

    private val _locationUpdates: Flow<LocationRecord> = callbackFlow {
        val callbackProviderImpl = object : LocationCallbackProvider {

            override fun onResultReceived(locationRecord: LocationRecord) {
                println("new location record received!")
                trySend(locationRecord)
            }
        }

        locationDatasource.setCallbackProvider(callbackProviderImpl)
        locationDatasource.subscribe()

        awaitClose {
            // close location data listener
            locationDatasource.unsubscribe()
        }
    }

    @ExperimentalCoroutinesApi
    override fun locationFlow(): Flow<LocationRecord> {
        return _locationUpdates
    }
}

interface LocationCallbackProvider {
    fun onResultReceived(locationRecord: LocationRecord)
}

interface ILocationDataSource {
    var locationServiceNativeClient: LocationServiceNativeClient
    fun subscribe()
    fun unsubscribe()
    fun setCallbackProvider(provider: LocationCallbackProvider)
}
enum class LocationPermissionStatus {
    Given, Revoked, Undefined
}