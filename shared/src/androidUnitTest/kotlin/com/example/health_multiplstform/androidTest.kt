package com.example.health_multiplstform

import CarouselWithHealthMetrics
import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.health_multiplstform.repos.HealthRecordsRepository
import com.example.health_multiplstform.ui.AppViewWithBottomNavigation
import com.example.health_multiplstform.ui.HealthRecordsScreen
import com.example.health_multiplstform.ui.MyTheme
import com.example.health_multiplstform.ui.PagerViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class AndroidViewModelTest {

    @get:Rule
    val composeRule = createComposeRule()
   // @Test
    fun testExample() {
        val dispatcher = StandardTestDispatcher()
        val scope = TestScope(dispatcher)
//        scope.runTest {
//
//        }
        composeRule.setContent {
            MyTheme {
                //AppViewWithBottomNavigation(DependencyManager())
                HealthRecordsScreen(flow {  })
            }
        }
    }
}