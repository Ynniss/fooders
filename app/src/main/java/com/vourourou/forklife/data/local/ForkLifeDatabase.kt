package com.vourourou.forklife.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vourourou.forklife.data.local.entity.ScanHistoryItem

@Database(
    entities = [ScanHistoryItem::class],
    version = 2,
    exportSchema = false
)
abstract class ForkLifeDatabase : RoomDatabase() {
    abstract fun scanHistoryDao(): ScanHistoryDao

    companion object {
        /**
         * Migration from version 1 to 2: Add allergenTags column
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE scan_history ADD COLUMN allergenTags TEXT DEFAULT NULL")
            }
        }
    }
}
