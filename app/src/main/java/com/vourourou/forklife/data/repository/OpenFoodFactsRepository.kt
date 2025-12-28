package com.vourourou.forklife.data.repository

import android.util.Log
import com.vourourou.forklife.data.remote.OpenFoodFactsApi
import com.vourourou.forklife.data.remote.model.Product
import com.vourourou.forklife.utils.Resource
import javax.inject.Inject

class OpenFoodFactsRepository @Inject constructor(
    private val api: OpenFoodFactsApi
) {
    suspend fun getProduct(barcode: String): Resource<Product> {
        return try {
            val response = api.getProduct(barcode)

            if (response.isSuccessful) {
                val body = response.body()

                when {
                    body == null -> Resource.Error("Empty response from server")
                    body.status == 1 && body.product != null -> Resource.Success(body.product)
                    body.status == 0 -> Resource.Error("Product not found. You might want to add it.")
                    else -> Resource.Error("Product not found")
                }
            } else {
                Resource.Error("HTTP ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("OpenFoodFactsRepo", "Error fetching product", e)
            Resource.Error(e.message ?: "An error occurred while fetching the product")
        }
    }
}
