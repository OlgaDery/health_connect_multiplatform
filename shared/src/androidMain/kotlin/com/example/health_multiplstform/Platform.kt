package com.example.health_multiplstform

import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
    override val uuid: String = UUID.randomUUID().toString()
}

actual fun getPlatform(): Platform = AndroidPlatform()

@Composable
actual fun <T> Flow<List<T>>.collectStateFlowForPlatform(): State<List<T>> {
    return this.collectAsStateWithLifecycle(listOf())
}

actual fun getScreenHeight(): Double {
    return Resources.getSystem().displayMetrics.heightPixels.toDouble()
}