package com.esgi.fooders.data.remote.responses.ImageModificationResponse

data class File(
    val code: String,
    val filename: String,
    val name: String,
    val thumbnailUrl: String,
    val url: String
)