package com.esgi.fooders.data.remote.responses.ModifyProductImageResponse

data class ModifyProductImageResponse(
    val code: String,
    val files: List<File>,
    val image: Image,
    val imagefield: String,
    val imgid: Int,
    val status: String
)