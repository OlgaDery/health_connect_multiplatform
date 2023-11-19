package com.example.health_multiplstform.utils

import epicarchitect.calendar.compose.basis.EpicMonth
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

interface IDataTypesProvider {
    val now: Long

    fun currentMonth(): Month
    fun monthNumber(time: Long): Int
    fun dayOfMonth(milisecs: Long): Int
}

class DataTypesProvider() : IDataTypesProvider {
    override val now: Long
        get() {
            return Clock.System.now().toEpochMilliseconds()
        }

    fun stringValue(milisecs: Long): String {
        return Instant.fromEpochMilliseconds(milisecs).toString().substring(0, 16)
    }

    override fun monthNumber(time: Long): Int {
        return Instant.fromEpochMilliseconds(time)
            .toLocalDateTime(timeZone = TimeZone.currentSystemDefault()).monthNumber
    }

    override fun dayOfMonth(milisecs: Long): Int {
        return Instant.fromEpochMilliseconds(milisecs)
            .toLocalDateTime(timeZone = TimeZone.currentSystemDefault()).dayOfMonth
    }

    override fun currentMonth(): Month {
        return EpicMonth.now().month
    }
}