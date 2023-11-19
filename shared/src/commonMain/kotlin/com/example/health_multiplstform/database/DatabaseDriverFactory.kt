package com.example.health_multiplstform.database

import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory() {
    fun createDriver(context: Any?): SqlDriver?
}