package com.esgi.fooders.ui.profile.viewpager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.esgi.fooders.R
import com.esgi.fooders.data.remote.responses.UserSuccessResponse.Succes

class SuccessAdapter() : RecyclerView.Adapter<SuccessViewHolder>() {
    private var successData: List<Succes>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuccessViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.success_list_item, parent, false)
        return SuccessViewHolder(view)
    }

    override fun onBindViewHolder(holder: SuccessViewHolder, position: Int) {
        holder.bind(successData!![position])
    }

    override fun getItemCount(): Int {
        return if (successData == null) 0 else successData!!.size
    }

    fun setSuccessData(successData: List<Succes>?) {
        this.successData = successData
        notifyDataSetChanged()
    }
}
