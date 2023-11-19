package com.example.health_multiplstform.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.health_multiplstform.collectStateFlowForPlatform
import kotlinx.coroutines.flow.Flow

@Composable
fun HealthRecordsScreen(flow: Flow<List<RecordToDisplay>>) {
    val list = flow.collectStateFlowForPlatform()
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (list.value.isEmpty()) {
            Column (
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.height(100.dp),
                    text = "No records collected so far"
                )
            }
        } else {
            Column (
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    modifier = Modifier.wrapContentHeight(),
                    text = "My records:"
                )

                RecyclerViewScreen (
                    list = list,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(end = 24.dp, start = 24.dp, bottom = 24.dp)
                        .weight(1.0F)
                )
            }
        }
    }
}