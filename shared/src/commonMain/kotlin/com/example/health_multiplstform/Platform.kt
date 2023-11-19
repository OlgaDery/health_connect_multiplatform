package com.example.health_multiplstform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import kotlinx.coroutines.flow.Flow

interface Platform {
    val name: String
    val uuid: String
}

expect fun getPlatform(): Platform

@Composable
expect fun<T> Flow<List<T>>.collectStateFlowForPlatform(): State<List<T>>

expect fun getScreenHeight(): Double