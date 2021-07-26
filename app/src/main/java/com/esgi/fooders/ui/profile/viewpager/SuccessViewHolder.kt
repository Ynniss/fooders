package com.esgi.fooders.ui.profile.viewpager

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.esgi.fooders.R

class SuccessViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // Find all the views of the list item
    private val successNameTextView: TextView = itemView.findViewById(R.id.txt_success_item)

    // Show the data in the views
    fun bind(data: String?) {
        successNameTextView.text = data

        // Since the data in these can be null we check and bind data
        // or remove the view otherwise
        bindOrHideTextView(successNameTextView, data)
    }

    private fun bindOrHideTextView(textView: TextView, data: String?) {
        if (data == null) {
            textView.visibility = View.GONE
        } else {
            textView.text = data
            textView.visibility = View.VISIBLE
        }
    }
}
