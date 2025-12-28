package com.vourourou.forklife.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vourourou.forklife.data.local.entity.ScanHistoryItem

@Database(
    entities = [ScanHistoryItem::class],
    version = 1,
    exportSchema = false
)
abstract class ForkLifeDatabase : RoomDatabase() {
    abstract fun scanHistoryDao(): ScanHistoryDao
}
