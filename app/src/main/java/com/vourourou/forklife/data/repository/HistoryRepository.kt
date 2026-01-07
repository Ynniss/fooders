package com.vourourou.forklife.data.repository

import com.google.gson.Gson
import com.vourourou.forklife.data.local.ScanHistoryDao
import com.vourourou.forklife.data.local.entity.ScanHistoryItem
import com.vourourou.forklife.data.remote.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryRepository @Inject constructor(
    private val scanHistoryDao: ScanHistoryDao,
    private val gson: Gson
) {

    fun getAllHistory(): Flow<List<ScanHistoryItem>> {
        return scanHistoryDao.getAllHistory()
    }

    // Stats
    fun getUniqueProductsCount(): Flow<Int> = scanHistoryDao.getUniqueProductsCount()
    fun getTotalScansCount(): Flow<Int?> = scanHistoryDao.getTotalScansCount()
    fun getMostScannedProducts(): Flow<List<ScanHistoryItem>> = scanHistoryDao.getMostScannedProducts()
    fun getLastScannedProduct(): Flow<ScanHistoryItem?> = scanHistoryDao.getLastScannedProduct()

    suspend fun insertScanHistory(product: Product) {
        val existingItem = scanHistoryDao.getByBarcode(product.code)
        val allergenTagsString = product.allergens_tags?.joinToString(",")
        val productJsonString = gson.toJson(product)

        if (existingItem != null) {
            val updatedItem = existingItem.copy(
                scanCount = existingItem.scanCount + 1,
                scannedAt = System.currentTimeMillis(),
                allergenTags = allergenTagsString ?: existingItem.allergenTags,
                productJson = productJsonString
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
                scanCount = 1,
                allergenTags = allergenTagsString,
                productJson = productJsonString
            )
            scanHistoryDao.insert(newItem)
        }
    }

    suspend fun getProductByBarcode(barcode: String): Product? {
        val historyItem = scanHistoryDao.getByBarcode(barcode)
        return historyItem?.productJson?.let { json ->
            try {
                gson.fromJson(json, Product::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun deleteByBarcode(barcode: String) {
        scanHistoryDao.deleteByBarcode(barcode)
    }

    suspend fun clearAll() {
        scanHistoryDao.clearAll()
    }
}
