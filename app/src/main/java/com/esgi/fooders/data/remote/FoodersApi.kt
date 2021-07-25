package com.esgi.fooders.data.remote

import com.esgi.fooders.data.remote.requests.LoginRequest
import com.esgi.fooders.data.remote.responses.ImageModificationResponse.ImageModificationResponse
import com.esgi.fooders.data.remote.responses.LoginResponse
import com.esgi.fooders.data.remote.responses.ProductInformations.InformationsModificationResponse
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
    suspend fun postProductImageModifications(
        @Body body: RequestBody,
    ): Response<ImageModificationResponse>


    @Headers("Content-Type: multipart/form-data")
    @POST("https://world.openfoodfacts.org/cgi/product_jqm2.pl")
    suspend fun postProductModifications(
        @Body body: RequestBody,
    ): Response<InformationsModificationResponse>

}