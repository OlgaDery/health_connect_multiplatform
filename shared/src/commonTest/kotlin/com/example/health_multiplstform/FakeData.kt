package com.example.health_multiplstform

import com.example.health_multiplstform.models.ConsolidatedRecord
import com.example.health_multiplstform.models.LocationRecord

object FakeData {

    val givenHealthRecord = ConsolidatedRecord(
        bodyTemperature = 11.0, heartBeat = 11L, time = 22222222222222222, stepsMadeSinceLastRecord = 8L)

    val givenHealthRecordFromNovember10 = ConsolidatedRecord(
        bodyTemperature = 11.0, heartBeat = 11L, time = 1699649829917, stepsMadeSinceLastRecord = 8L)

    val givenHealthRecord1 = ConsolidatedRecord(
        bodyTemperature = 11.0, heartBeat = 11L, time = 2222222222222222222, stepsMadeSinceLastRecord = 8L)

    val givenLocationRecord = LocationRecord(idOfSourceRecord = null, latitude = 11.11, longitude = 12.12, timestamp = 123)
    val givenMockRecordWithTimeMatchingHealthRecord1 = LocationRecord(idOfSourceRecord = null, latitude = 11.11, longitude = 12.12, timestamp = 2222222222222222223)
}