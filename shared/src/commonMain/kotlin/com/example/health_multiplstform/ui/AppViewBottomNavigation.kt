package com.example.health_multiplstform.ui
import CarouselWithHealthMetrics
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.Text
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.lightColors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.chrynan.navigation.ExperimentalNavigationApi
import com.chrynan.navigation.compose.NavigationContainer
import com.chrynan.navigation.compose.rememberSavableNavigator
import com.chrynan.navigation.push
import com.example.health_multiplstform.DependencyManager
import com.example.health_multiplstform.getScreenHeight
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalNavigationApi::class, ExperimentalSerializationApi::class)
@Composable
fun AppViewWithBottomNavigation(dependencyManager: DependencyManager) {

    val navigator = rememberSavableNavigator(Destinations.Home.name)

    val screens = listOf(
        Destinations.Home.name,
        Destinations.Metrics.name,
        Destinations.AddRecord.name,
        Destinations.DeleteAll.name)

    var selectedScreen by rememberSaveable { mutableStateOf(screens.first()) }

    Scaffold(
        bottomBar = {
            BottomNavigation(
                backgroundColor = lightColors().background
            ) {
                screens.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(imageVector = getIconForScreen(screen), contentDescription = "") },
                        label = { Text(screen) },
                        selected = selectedScreen == screen,
                        onClick = {
                            selectedScreen = screen
                            navigator.push(screen)
                        }
                    )
                }
            }
        }
    )
    { innerPadding ->

        val pagerViewModel = getViewModel(
            key = "PagerViewModel",
            factory = viewModelFactory {
                PagerViewModel(dependencyManager.healthRepo)
            }
        )

        val recordsListViewModel = getViewModel(
            key = "RecordsViewModel",
            factory = viewModelFactory {
                RecordsListViewModel(
                    dependencyManager.healthRepo,
                    dependencyManager.locationDataRepository,
                    dependencyManager.dataTypesProvider
                )
            }
        )

        val dataEntryViewModel = getViewModel(
            key = "RecordEntryViewModel",
            factory = viewModelFactory {
                RecordEntryViewModel(
                    dependencyManager.healthRepo,
                    dependencyManager.locationDataRepository
                )
            }
        )

        NavigationContainer(navigator = navigator) { (destination, context) ->
            when (selectedScreen) {
                Destinations.Home.name -> HealthRecordsScreen(
                    flow = recordsListViewModel.recordsToDisplay)
                Destinations.DeleteAll.name -> DeleteRecordsByDateScreen(
                   viewModel = recordsListViewModel
                )
                Destinations.Metrics.name -> CarouselWithHealthMetrics(
                    viewModel = pagerViewModel)
                Destinations.AddRecord.name -> HealthDataEntryScreen(
                    viewModel = dataEntryViewModel
                )
            }
        }

    }
}

@Composable
fun getIconForScreen(screen: String): ImageVector {
    return when (screen) {
        Destinations.Home.name -> Icons.Default.Build
        Destinations.Metrics.name -> Icons.Default.Person
        Destinations.AddRecord.name -> Icons.Default.Add
        else -> Icons.Default.Delete
    }
}

enum class Destinations() {
    Home, Metrics, AddRecord, DeleteAll
}