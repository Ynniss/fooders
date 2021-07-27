package com.esgi.fooders.data.remote.responses.RankingResponse

import com.google.gson.annotations.SerializedName

data class RankingData(
    @SerializedName("id")
    val username: String,
    val photoStat: Int,
    val textStat: Int,
    val scanStat: Int,
    val rankingType: String
)