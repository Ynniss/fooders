package com.esgi.fooders.data.repository

import com.esgi.fooders.data.remote.FoodersApi
import com.esgi.fooders.data.remote.responses.ProductInformations.ProductInformationsResponse
import com.esgi.fooders.utils.Resource
import javax.inject.Inject

class ScanRepository @Inject constructor(
    private val api: FoodersApi
) {
    suspend fun getProductInformations(barcode: String): Resource<ProductInformationsResponse> {
        return try {
            val response = api.getProductInformations(barcode)

            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else if (response.code() == 401) {
                Resource.Error("Barcode missing.")
            } else {
                Resource.Error("Product Not found")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occured while requesting Open Food Fact API")
        }
    }
}