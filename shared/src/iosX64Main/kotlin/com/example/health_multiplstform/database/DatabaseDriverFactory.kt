package com.example.health_multiplstform.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.example.health_multiplstform.shared.cache.AppDatabase

actual class DatabaseDriverFactory actual constructor() {
    actual fun createDriver(context: Any?): SqlDriver? {
        return NativeSqliteDriver(AppDatabase.Schema, "test.db")
    }
}