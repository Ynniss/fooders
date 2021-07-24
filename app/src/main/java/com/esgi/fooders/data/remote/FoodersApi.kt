package com.esgi.fooders.data.remote

import com.esgi.fooders.data.remote.requests.LoginRequest
import com.esgi.fooders.data.remote.responses.LoginResponse
import com.esgi.fooders.data.remote.responses.ModifyProductImageResponse.ModifyProductImageResponse
import com.esgi.fooders.data.remote.responses.ProductInformations.ProductInformationsResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface FoodersApi {
    @Headers("Content-Type: application/json")
    @POST("login/")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @Headers("Content-Type: application/json")
    @GET("product/{barcode}")
    suspend fun getProductInformations(
        @Path("barcode") barcode: String = "dsqdsq"
    ): Response<ProductInformationsResponse>


    @Headers("Content-Type: multipart/form-data")
    @POST("https://world.openfoodfacts.org/cgi/product_image_upload.pl")
    suspend fun postProductImage(
        @Body body: RequestBody,
    ): Response<ModifyProductImageResponse>

}