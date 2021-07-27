package com.esgi.fooders.ui.profile.viewpager

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.esgi.fooders.R
import com.esgi.fooders.data.remote.responses.RankingResponse.RankingData

class RankingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // Find all the views of the list item
    private val rankingUsernameTextView: TextView = itemView.findViewById(R.id.txt_ranking_username_item)
    private val rankingScoreTextView: TextView = itemView.findViewById(R.id.txt_ranking_score_item)

    // Show the data in the views
    fun bind(data: RankingData?) {

        val score : String  = if (data?.rankingType == "photo") {
            data.photoStat.toString()
        } else if (data?.rankingType == "scan") {
            data.scanStat.toString()
        } else if (data?.rankingType == "text") {
            data.txtStat.toString()
        } else {
            "not found"
        }
        rankingUsernameTextView.text = data?.username ?: "N/C"
        rankingScoreTextView.text = score
    }
}
