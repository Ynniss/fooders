package com.esgi.fooders.data.remote.responses.ImageModificationResponse

data class Image(
    val crop_url: String,
    val imgid: Int,
    val thumb_url: String
)