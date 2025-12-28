package com.vourourou.forklife.data.remote

import com.vourourou.forklife.data.remote.model.OpenFoodFactsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenFoodFactsApi {
    @GET("api/v2/product/{barcode}.json")
    suspend fun getProduct(@Path("barcode") barcode: String): Response<OpenFoodFactsResponse>
}
