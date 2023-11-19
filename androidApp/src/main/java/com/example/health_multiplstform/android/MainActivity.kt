package com.example.health_multiplstform.android

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.BasalBodyTemperatureRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.lifecycle.lifecycleScope
import com.example.health_multiplstform.MainView
import com.example.health_multiplstform.locationDataSource.LocationPermissionStatus
import com.example.health_multiplstform.models.HealthDataProvider
import com.example.health_multiplstform.models.LocationRecord
import com.example.health_multiplstform.ui.HealthRecordListItem
import com.example.health_multiplstform.ui.HealthRecordsScreen
import com.example.health_multiplstform.ui.RecordToDisplay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    val PERMISSIONS =
        setOf(
            HealthPermission.getReadPermission(HeartRateRecord::class),
            HealthPermission.getReadPermission(StepsRecord::class),
            HealthPermission.getReadPermission(BasalBodyTemperatureRecord::class),
            HealthPermission.getReadPermission(BloodPressureRecord::class)
        )

    val healthPermissions =
        registerForActivityResult(PermissionController.createRequestPermissionResultContract()) { granted ->
            if (granted.containsAll(PERMISSIONS)) {
                // Permissions successfully granted
                // PERMISSIONS: Set<string> as of Alpha11
            } else {
                // Lack of required permissions
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainView(App.dependencyManager)
        }
    }

    override fun onResume() {
        super.onResume()
        enableMyLocation()
    }

    suspend fun checkPermissionsAndRun(healthConnectClient: HealthConnectClient) {
        // For alpha09 and lower versions, use getGrantedPermissions(PERMISSIONS) instead
        val granted = healthConnectClient.permissionController.getGrantedPermissions()
        if (granted.containsAll(PERMISSIONS)) {
            // Permissions already granted; proceed with inserting or reading data.
            App.dependencyManager.healthRepo.launchDataCollectionJob()
        } else {
            healthPermissions.launch(PERMISSIONS)
        }
    }

    private fun enableMyLocation() {
        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            )
        {
            App.dependencyManager.locationDataRepository.startDataCollectionJob()
            lifecycleScope.launch {
                App.dependencyManager.healthConnectClient?.let {
                    checkPermissionsAndRun(it)
                }
            }
            // 2. If if a permission rationale dialog should be shown
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        ) {
            App.dependencyManager.locationDataRepository.startDataCollectionJob()
            lifecycleScope.launch {
                App.dependencyManager.healthConnectClient?.let {
                    checkPermissionsAndRun(it)
                }
            }
        } else {
            // 3. Otherwise, request permission
            mutableListOf<String>().apply {
                add(android.Manifest.permission.ACCESS_FINE_LOCATION)
                add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    add(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                }
                ActivityCompat.requestPermissions(
                    this@MainActivity, this.toTypedArray(),1
                )
            }
            App.dependencyManager.locationDataRepository.stopDataCollectionJob()
        }
    }
}

@Preview(device = "id:pixel_5")
@Composable
fun DefaultPreview() {
    val testStream = flow {
        mutableListOf<RecordToDisplay>().apply {
            add(
                RecordToDisplay(
                    temperature = "11.1",
                    steps = "7",
                    heartBeat = "66",
                    time = "Oct 12 2022",
                    healthDataProvider = HealthDataProvider.PlatformHealthProvider,
                    locationRecord = LocationRecord(
                        longitude = 77.77,
                        latitude = 77.77,
                        timestamp = 123,
                        idOfSourceRecord = null)
                )
            )
            add(
                RecordToDisplay(
                    temperature = "11.1",
                    steps = "7",
                    heartBeat = "66",
                    time = "Oct 12 2022",
                    healthDataProvider = HealthDataProvider.UserInput,
                    locationRecord = null)
            )
            emit(this)
        }
    }
    HealthRecordsScreen(flow = testStream)
}

@Preview(device = "id:pixel_5")
@Composable
fun HealthRecordItemPreview() {
    HealthRecordListItem(data = RecordToDisplay(
        temperature = "36.6",
        time = "Tue 22 October 2023",
        steps = "5",
        heartBeat = "33",
        healthDataProvider = HealthDataProvider.PlatformHealthProvider,
        locationRecord = null
    ))
}

@Preview(heightDp = 200, widthDp = 950)
@Composable
fun HealthRecordItemPreviewHorizontal() {
    HealthRecordListItem(data = RecordToDisplay(
        temperature = "36.6",
        time = "Tue 22 October 2023",
        steps = "5",
        heartBeat = "33",
        healthDataProvider = HealthDataProvider.PlatformHealthProvider,
        locationRecord = null
    ))
}
