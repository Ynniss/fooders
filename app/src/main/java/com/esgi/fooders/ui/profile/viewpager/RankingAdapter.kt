package com.esgi.fooders.ui.profile.viewpager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.esgi.fooders.R
import com.esgi.fooders.data.remote.responses.RankingResponse.RankingData

class RankingAdapter : RecyclerView.Adapter<RankingViewHolder>() {
    private var rankingData: List<RankingData>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.ranking_list_item, parent, false)
        return RankingViewHolder(view)
    }

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        holder.bind(rankingData!![position])
    }

    override fun getItemCount(): Int {
        return if (rankingData == null) 0 else rankingData!!.size
    }

    fun setRankingData(data: List<RankingData>?) {
        this.rankingData = data
        notifyDataSetChanged()
    }
}
