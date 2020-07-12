package com.eventersapp.consumer.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eventersapp.consumer.R
import com.eventersapp.consumer.data.model.AllEventListResponse
import com.eventersapp.consumer.util.AppConstants
import com.eventersapp.consumer.util.AppUtils
import com.eventersapp.consumer.util.AppUtils.base64StringToImage
import com.eventersapp.consumer.util.show

class CustomAdapterAllEvent : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ITEM = 1
        private const val TYPE_LOADING = 2
    }

    private var allEventList = ArrayList<AllEventListResponse.Data?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ITEM -> {
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_all_event, parent, false)
                ItemViewHolder(v)
            }
            else -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_lazy_loading, parent, false)
                LoadingViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder -> {
                allEventList[position]?.let { holder.bindItems(it) }
            }
            is LoadingViewHolder -> {
                holder.showLoadingView()
            }
        }
    }

    override fun getItemCount(): Int {
        return allEventList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            allEventList[position] == null -> TYPE_LOADING
            else -> TYPE_ITEM
        }
    }

    fun getListData() = allEventList

    fun setData(newAllEvent: ArrayList<AllEventListResponse.Data?>?) {
        if (newAllEvent != null) {
            if (allEventList.isNotEmpty())
                allEventList.removeAt(allEventList.size - 1)
            allEventList.clear()
            allEventList.addAll(newAllEvent)
        } else {
            allEventList.add(newAllEvent)
        }
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textEventTitle: TextView = itemView.findViewById(R.id.text_event_title)
        private val textEventDescription: TextView =
            itemView.findViewById(R.id.text_event_description)
        private val textDateTime: TextView = itemView.findViewById(R.id.text_date_time)
        private val textTicketPrice: TextView = itemView.findViewById(R.id.text_ticket_price)
        private val imageEventImage: ImageView = itemView.findViewById(R.id.image_event_image)

        @SuppressLint("SetTextI18n")
        fun bindItems(allEvent: AllEventListResponse.Data) {
            allEvent.publicEvent.let {
                textEventTitle.text = it.eventTitle
                textEventDescription.text = it.eventDescription
                textDateTime.text = AppUtils.changeDateFormat(
                    it.dateTime,
                    AppConstants.DISPLAY_DATE_FORMAT,
                    AppConstants.API_DATE_FORMAT
                )
                textTicketPrice.text = "$ ${it.ticketPrice}"
                if (it.eventImage != null) {
                    imageEventImage.visibility = View.VISIBLE
                    imageEventImage.setImageBitmap(base64StringToImage(it.eventImage))
                } else {
                    imageEventImage.visibility = View.GONE
                }
            }

        }

    }

    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)

        fun showLoadingView() {
            progressBar.show()
        }
    }

}