package com.vourourou.forklife.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scan_history")
data class ScanHistoryItem(
    @PrimaryKey
    val barcode: String,
    val productName: String,
    val imageUrl: String?,
    val nutriscoreGrade: String?,
    val ecoscoreGrade: String?,
    val novaGroup: Int?,
    val scannedAt: Long,
    val scanCount: Int = 1
)
