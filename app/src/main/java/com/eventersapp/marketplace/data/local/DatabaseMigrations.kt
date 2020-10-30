package com.eventersapp.marketplace.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.eventersapp.marketplace.data.model.Account

object DatabaseMigrations {
    const val DB_VERSION = 1

    val MIGRATIONS: Array<Migration>
        get() = arrayOf<Migration>(
            migration()
        )

    private fun migration(): Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE ${Account.TABLE_NAME} ADD COLUMN body TEXT")
        }
    }
}
