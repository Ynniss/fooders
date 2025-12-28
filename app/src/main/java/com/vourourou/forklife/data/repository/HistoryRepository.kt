package com.vourourou.forklife.data.repository

import com.vourourou.forklife.data.local.ScanHistoryDao
import com.vourourou.forklife.data.local.entity.ScanHistoryItem
import com.vourourou.forklife.data.remote.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryRepository @Inject constructor(
    private val scanHistoryDao: ScanHistoryDao
) {

    fun getAllHistory(): Flow<List<ScanHistoryItem>> {
        return scanHistoryDao.getAllHistory()
    }

    suspend fun insertScanHistory(product: Product) {
        val existingItem = scanHistoryDao.getByBarcode(product.code)

        if (existingItem != null) {
            val updatedItem = existingItem.copy(
                scanCount = existingItem.scanCount + 1,
                scannedAt = System.currentTimeMillis()
            )
            scanHistoryDao.insert(updatedItem)
        } else {
            val newItem = ScanHistoryItem(
                barcode = product.code,
                productName = product.product_name!!.ifEmpty { "Unknown Product" },
                imageUrl = product.image_front_url,
                nutriscoreGrade = product.nutriscore_grade,
                ecoscoreGrade = product.ecoscore_grade,
                novaGroup = product.nova_group,
                scannedAt = System.currentTimeMillis(),
                scanCount = 1
            )
            scanHistoryDao.insert(newItem)
        }
    }

    suspend fun deleteByBarcode(barcode: String) {
        scanHistoryDao.deleteByBarcode(barcode)
    }

    suspend fun clearAll() {
        scanHistoryDao.clearAll()
    }
}
