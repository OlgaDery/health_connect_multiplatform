package com.example.health_multiplstform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.flow.Flow
import platform.UIKit.UIScreen

@Composable
actual fun <T> Flow<List<T>>.collectStateFlowForPlatform(): State<List<T>> {
    return this.collectAsState(listOf())
}

@OptIn(ExperimentalForeignApi::class)
actual fun getScreenHeight(): Double {
   UIScreen.mainScreen().bounds.useContents {
       return this.size.height
   }
}