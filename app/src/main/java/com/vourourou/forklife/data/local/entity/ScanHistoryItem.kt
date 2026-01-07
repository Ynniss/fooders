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
    val scanCount: Int = 1,
    val allergenTags: String? = null,  // Comma-separated allergen tags for storage
    val productJson: String? = null     // Full Product object as JSON for offline viewing
) {
    /**
     * Parses the allergenTags string into a list of tags.
     */
    fun getAllergenTagsList(): List<String> {
        return allergenTags?.split(",")?.filter { it.isNotBlank() } ?: emptyList()
    }
}
