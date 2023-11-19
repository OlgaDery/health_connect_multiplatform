package com.example.health_multiplstform.ui

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.cinterop.ExperimentalForeignApi
import platform.MapKit.MKAnnotationProtocol
import platform.MapKit.MKMapView
import platform.MapKit.MKMapViewDelegateProtocol
import platform.darwin.NSObject

@Composable
actual fun MapToEnterLocation(
    modifier: Modifier,
    size: Dp,
    viewModel: RecordEntryViewModel
) {
}