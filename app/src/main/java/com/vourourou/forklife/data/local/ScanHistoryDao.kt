package com.vourourou.forklife.data.local

import androidx.room.*
import com.vourourou.forklife.data.local.entity.ScanHistoryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanHistoryDao {

    @Query("SELECT * FROM scan_history ORDER BY scannedAt DESC")
    fun getAllHistory(): Flow<List<ScanHistoryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ScanHistoryItem)

    @Query("DELETE FROM scan_history WHERE barcode = :barcode")
    suspend fun deleteByBarcode(barcode: String)

    @Query("DELETE FROM scan_history")
    suspend fun clearAll()

    @Query("SELECT * FROM scan_history WHERE barcode = :barcode LIMIT 1")
    suspend fun getByBarcode(barcode: String): ScanHistoryItem?

    // Stats queries
    @Query("SELECT COUNT(*) FROM scan_history")
    fun getUniqueProductsCount(): Flow<Int>

    @Query("SELECT SUM(scanCount) FROM scan_history")
    fun getTotalScansCount(): Flow<Int?>

    @Query("SELECT * FROM scan_history ORDER BY scanCount DESC LIMIT 5")
    fun getMostScannedProducts(): Flow<List<ScanHistoryItem>>

    @Query("SELECT * FROM scan_history ORDER BY scannedAt DESC LIMIT 1")
    fun getLastScannedProduct(): Flow<ScanHistoryItem?>
}
