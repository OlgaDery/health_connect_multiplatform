package com.example.health_multiplstform.android

import android.app.Application
import androidx.health.connect.client.HealthConnectClient
import com.example.health_multiplstform.DependencyManager

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        dependencyManager = DependencyManager(this.applicationContext, getHealthClient())

    }

    fun getHealthClient(): HealthConnectClient? {
        val availabilityStatus = HealthConnectClient.sdkStatus(this,"com.google.android.apps.healthdata")
        if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE) {
            return null// early return as there is no viable integration
        }
        if (availabilityStatus == HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED) {
            // Optionally redirect to package installer to find a provider, for example:
            //   val uriString = "market://details?id=$providerPackageName&url=healthconnect%3A%2F%2Fonboarding"
            return null
        }
        val client = HealthConnectClient.getOrCreate(this)
        return client
    }

    companion object {
        lateinit var dependencyManager: DependencyManager
    }
}