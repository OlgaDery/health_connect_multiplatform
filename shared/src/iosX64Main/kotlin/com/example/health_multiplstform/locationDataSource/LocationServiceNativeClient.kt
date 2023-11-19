package com.example.health_multiplstform.locationDataSource

import com.example.health_multiplstform.models.LocationRecord
import com.example.health_multiplstform.utils.DataTypesProvider
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLDistanceFilterNone
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.Foundation.NSError
import platform.darwin.NSObject

actual class LocationServiceNativeClient(
    private val nativeClient: CLLocationManager?
): NSObject(), CLLocationManagerDelegateProtocol {

    actual var callbackProvider: LocationCallbackProvider? = null
    lateinit var dataCollectionJobCancellationCallback: (() -> Unit)
    lateinit var dataCollectionJobStartCallback: (() -> Unit)
    init {
        // Assign the locationDelegate to the CLLocationManager delegate
        nativeClient?.delegate = this
    }

    actual fun subscribeToServices() {
        println("subscribe to services")
        nativeClient?.desiredAccuracy = kCLLocationAccuracyBest
        nativeClient?.distanceFilter = kCLDistanceFilterNone
        nativeClient?.allowsBackgroundLocationUpdates = true
        nativeClient?.startUpdatingLocation()
    }

    actual fun unsubscribeFromServices() {
        nativeClient?.stopUpdatingLocation()
    }

    actual fun updateCallbackProvider(newCallbackProvider: LocationCallbackProvider) {
        callbackProvider = newCallbackProvider
    }
    //location delegate functions
    @OptIn(ExperimentalForeignApi::class)
    override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
        println("location manager called")
        didUpdateLocations.firstOrNull()?.let {
            val location = it as CLLocation
            location.coordinate.useContents {
                val record = LocationRecord(
                    latitude = latitude,
                    longitude = longitude,
                    timestamp = DataTypesProvider().now,
                    idOfSourceRecord = null
                )
                println("location updated!")
                callbackProvider?.onResultReceived(record)
            }
        }
    }

    override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
        println("autorization status updated")
        when (manager.authorizationStatus) {
            1, 2 -> {
                dataCollectionJobCancellationCallback()
            } // cancel current job to collect flow
            3, 4 -> {
                println("start location job")
                dataCollectionJobStartCallback()
            } //start job to collect flow
        }
    }

    override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
        //add code
    }

}