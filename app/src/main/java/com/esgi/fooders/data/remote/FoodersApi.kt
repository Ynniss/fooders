package com.esgi.fooders.data.remote

import com.esgi.fooders.data.remote.requests.LoginRequest
import com.esgi.fooders.data.remote.responses.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface FoodersApi {
    @Headers("Content-Type: application/json")
    @POST("login/")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

}