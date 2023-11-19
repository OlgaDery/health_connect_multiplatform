package com.example.health_multiplstform.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.example.health_multiplstform.models.LocationRecord
import com.example.health_multiplstform.utils.DataTypesProvider
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
actual fun MapToEnterLocation(
    modifier: Modifier,
    size: Dp,
    viewModel: RecordEntryViewModel
) {

    val marker = rememberSaveable{ mutableStateOf(LatLng(
        0.0, 0.0)) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(marker.value, 30f)
    }

    Box(modifier = Modifier.wrapContentHeight()) {
        GoogleMap(
            modifier = Modifier.requiredHeight(size),
            cameraPositionState = cameraPositionState,
            onMapClick = {
                marker.value = it
                viewModel.inputLocation = LocationRecord(
                    it.longitude,
                    it.latitude,
                    DataTypesProvider().now,
                    null)
            }
        ) {
            Marker (
                state = MarkerState(position = marker.value),
                title = "Your selected location"
            )
        }
    }

}