package com.esgi.fooders.data.remote.responses.UserSuccessResponse


data class UserSuccessResponse(
    val data: Data
)

data class Data(
    val unlocked: List<String>
)