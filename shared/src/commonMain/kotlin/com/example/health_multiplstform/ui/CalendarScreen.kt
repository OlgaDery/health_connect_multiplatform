package com.example.health_multiplstform.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import epicarchitect.calendar.compose.basis.BasisDayOfMonthContent
import epicarchitect.calendar.compose.basis.BasisEpicCalendar
import epicarchitect.calendar.compose.basis.EpicMonth
import epicarchitect.calendar.compose.basis.atStartDay
import epicarchitect.calendar.compose.basis.config.rememberBasisEpicCalendarConfig
import epicarchitect.calendar.compose.basis.state.rememberBasisEpicCalendarState
import epicarchitect.calendar.compose.ranges.drawEpicRanges
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus

@Composable
fun DeleteRecordsByDateScreen(
    viewModel: RecordsListViewModel) {

    val currentMonth = viewModel.currentMonth
    val displayDates = viewModel.datesToDisplay.collectAsState(initial = listOf())

    Box(modifier = Modifier.padding(20.dp)) {

        LazyColumn {
            item {
                Text(
                    modifier = Modifier.wrapContentHeight().padding(bottom = 10.dp),
                    fontWeight = FontWeight.Bold,
                    text = "Highlighted dates are days in " + currentMonth.name + " when the health data was collected"
                )
            }

            item {
                BasisEpicCalendar(
                    state = rememberBasisEpicCalendarState(
                        config = rememberBasisEpicCalendarConfig(
                            rowsSpacerHeight = 4.dp,
                            dayOfWeekViewHeight = 40.dp,
                            dayOfMonthViewHeight = 40.dp,
                            columnWidth = 40.dp,
                            dayOfWeekViewShape = RoundedCornerShape(16.dp),
                            dayOfMonthViewShape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(0.dp),
                            contentColor = Color.Blue,
                            displayDaysOfAdjacentMonths = true,
                            displayDaysOfWeek = true
                        )
                    ),

                    onDayOfMonthClick = {
                        // delete records in both tables

                    },
                    dayOfMonthContent = {
                        var color: Color = Color.White
                        if (displayDates.value.contains(it.dayOfMonth) && it.month == viewModel.currentMonth) {
                            color = Color.LightGray
                        }
                        Box(modifier = Modifier.background(color)) {
                            Text(it.dayOfMonth.toString())
                        }
                    }
                )

            }

            item {
                OutlinedButton(
                    modifier = Modifier.padding(10.dp),
                    onClick = {
                      viewModel.deleteAllRecords()
                    },
                    content = {
                        Text("Clear all data")
                    })
            }

        }
    }

}