package com.example.health_multiplstform.locationDataSource

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.example.health_multiplstform.models.LocationRecord
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.util.Date

actual class LocationServiceNativeClient (context: Context) {

    private var client: FusedLocationProviderClient? = null
    lateinit var locationCallback: LocationCallback
    actual var callbackProvider: LocationCallbackProvider? = null

    init {
        client = LocationServices.getFusedLocationProviderClient(context)
    }

    @SuppressLint("MissingPermission")
    actual fun subscribeToServices(
    ) {
        println("enter subscribeToServices!")
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {

                super.onLocationResult(p0)
                callbackProvider?.onResultReceived(LocationRecord(p0.lastLocation?.longitude ?: 0.0,
                    p0.lastLocation?.latitude ?: 0.0, timestamp = Date().time, idOfSourceRecord = null))
            }
        }
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY, 1000*60
        ).build()
        client?.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    actual fun unsubscribeFromServices() {
        println("enter unsubscribeFromServices")
        client?.removeLocationUpdates(locationCallback)
    }

    actual fun updateCallbackProvider(newCallbackProvider: LocationCallbackProvider) {
        callbackProvider = newCallbackProvider
    }
}