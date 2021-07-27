package com.esgi.fooders.data.remote.responses.RankingResponse

data class RankingResponse(
    val photoRanking: List<RankingData>,
    val scanRanking: List<RankingData>,
    val textRanking: List<RankingData>
)