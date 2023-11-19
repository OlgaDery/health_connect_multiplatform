package com.example.health_multiplstform.ui

import androidx.compose.runtime.Composable
import com.example.health_multiplstform.DependencyManager

@Composable
fun App(dependencyManager: DependencyManager) {
    MyTheme {
       AppViewWithBottomNavigation(dependencyManager)
    }
}



