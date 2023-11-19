package com.example.health_multiplstform.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.health_multiplstform.shared.cache.AppDatabase

actual class DatabaseDriverFactory actual constructor() {
    actual fun createDriver(context: Any?): SqlDriver? {
        return try {
            AndroidSqliteDriver(AppDatabase.Schema, context as Context, "test.db")
        } catch (e: Exception) {
            null
        }
    }
}