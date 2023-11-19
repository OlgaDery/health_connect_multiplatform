package com.example.health_multiplstform

import androidx.compose.ui.window.ComposeUIViewController
import com.example.health_multiplstform.ui.App
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val uuid: String
        get() = "UUID"
}

actual fun getPlatform(): Platform = IOSPlatform()

fun MainViewController(dependencyManager: DependencyManager) = ComposeUIViewController { App(dependencyManager) }