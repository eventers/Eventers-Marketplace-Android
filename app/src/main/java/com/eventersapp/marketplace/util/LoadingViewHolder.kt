package com.eventersapp.marketplace.util

import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.eventersapp.marketplace.R

class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)

    fun showLoadingView() {
        progressBar.show()
    }
}