package com.example.health_multiplstform.locationDataSource

actual class LocationServiceNativeClient {
    actual fun subscribeToServices() {}

    actual fun unsubscribeFromServices() {}
    actual fun updateCallbackProvider(newCallbackProvider: LocationCallbackProvider) {}
    actual var callbackProvider: LocationCallbackProvider? = null

}