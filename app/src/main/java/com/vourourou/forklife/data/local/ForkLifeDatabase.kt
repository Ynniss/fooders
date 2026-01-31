package com.vourourou.forklife.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vourourou.forklife.data.local.dao.ProfileDao
import com.vourourou.forklife.data.local.entity.IngredientEntity
import com.vourourou.forklife.data.local.entity.ProfileEntity
import com.vourourou.forklife.data.local.entity.ScanHistoryItem

@Database(
    entities = [
        ScanHistoryItem::class,
        ProfileEntity::class,
        IngredientEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class ForkLifeDatabase : RoomDatabase() {
    abstract fun scanHistoryDao(): ScanHistoryDao

    abstract fun profileDao(): ProfileDao

    companion object {
        /**
         * Migration from version 1 to 2: Add allergenTags column
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE scan_history ADD COLUMN allergenTags TEXT DEFAULT NULL")
            }
        }

        /**
         * Migration from version 2 to 3: Add productJson column for offline support
         */
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE scan_history ADD COLUMN productJson TEXT DEFAULT NULL")
            }
        }
    }
}
