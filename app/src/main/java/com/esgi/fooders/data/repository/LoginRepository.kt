package com.esgi.fooders.data.repository

import com.esgi.fooders.data.remote.FoodersApi
import com.esgi.fooders.data.remote.requests.LoginRequest
import com.esgi.fooders.data.remote.responses.BaseFoodersApiResponse
import com.esgi.fooders.utils.Resource
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val api: FoodersApi
) {
    suspend fun login(
        username: String,
        password: String,
        fcmToken: String
    ): Resource<BaseFoodersApiResponse> {
        return try {
            val response = api.login(
                LoginRequest(
                    username = username,
                    password = password,
                    fcmToken = fcmToken
                )
            )
            val result = response.body()

            when {
                response.isSuccessful && result != null -> Resource.Success(result)
                response.code() == 401 -> Resource.Error("Login failed. Check your credentials")
                else -> Resource.Error("An error occurred")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occured while requesting Open Food Fact API")
        }
    }
}