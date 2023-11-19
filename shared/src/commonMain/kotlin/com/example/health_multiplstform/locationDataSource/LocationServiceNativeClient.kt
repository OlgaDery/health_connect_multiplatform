package com.example.health_multiplstform.locationDataSource

expect class LocationServiceNativeClient {
    var callbackProvider: LocationCallbackProvider?
    fun updateCallbackProvider(newCallbackProvider: LocationCallbackProvider)
    fun subscribeToServices()
    fun unsubscribeFromServices()
}