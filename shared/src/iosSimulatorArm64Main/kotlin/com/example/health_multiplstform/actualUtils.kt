package com.example.health_multiplstform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.Flow

@Composable
actual fun <T> Flow<List<T>>.collectStateFlowForPlatform(): State<List<T>> {
    return this.collectAsState(listOf())
}

actual fun getScreenHeight(): Double {
   return 0.0
}