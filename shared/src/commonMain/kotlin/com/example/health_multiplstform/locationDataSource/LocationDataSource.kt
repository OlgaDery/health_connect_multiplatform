package com.example.health_multiplstform.locationDataSource

import com.example.health_multiplstform.models.LocationRecord
import kotlinx.coroutines.Job

class LocationDataSource(
    override var locationServiceNativeClient: LocationServiceNativeClient
) : ILocationDataSource {

    override fun subscribe() {
        locationServiceNativeClient.subscribeToServices()
    }
    override fun unsubscribe() {
        locationServiceNativeClient.unsubscribeFromServices()
    }
}