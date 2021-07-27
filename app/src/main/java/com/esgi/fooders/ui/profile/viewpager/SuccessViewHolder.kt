package com.esgi.fooders.ui.profile.viewpager

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.esgi.fooders.R
import com.esgi.fooders.data.remote.responses.UserSuccessResponse.Succes
import com.google.android.material.card.MaterialCardView

class SuccessViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // Find all the views of the list item
    private val successNameTextView: TextView = itemView.findViewById(R.id.txt_success_name_item)
    private val successDescTextView: TextView = itemView.findViewById(R.id.txt_success_desc_item)
    private val successStatusTextView: TextView =
        itemView.findViewById(R.id.txt_success_status_item)

    // Show the data in the views
    fun bind(data: Succes?) {
        successNameTextView.text = data?.name ?: "not found"
        successDescTextView.text = data?.description ?: "not found"
        successStatusTextView.text = data?.status ?: "not found"
    }
}
