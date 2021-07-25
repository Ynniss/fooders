package com.esgi.fooders.data.repository

import android.util.Log
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
            Log.e("SCAN REPO BODY", response.toString())
            if (response.isSuccessful && result?.message == "Product found") {
                Resource.Success(result)
            } else if (response.code() == 401) {
                Resource.Error("Barcode missing.")
            } else if (result?.message == "Product not found. You might want to add it.") {
                Resource.Error(result.message)
            } else {
                Log.e("SCAN REPO ELSE", response.body().toString())
                Resource.Error("Produit non trouvé")
            }
        } catch (e: Exception) {
            Log.e("SCAN REPO EXCEPTION", e.toString())
            Resource.Error(e.message ?: "Une erreur est survenue lors de la requête. Réessayez.")
        }
    }
}