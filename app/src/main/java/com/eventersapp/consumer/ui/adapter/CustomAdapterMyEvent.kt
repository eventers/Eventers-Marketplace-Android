package com.eventersapp.consumer.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eventersapp.consumer.R
import com.eventersapp.consumer.data.model.MyEventListResponse
import com.eventersapp.consumer.util.AppConstants.API_DATE_FORMAT
import com.eventersapp.consumer.util.AppConstants.DISPLAY_DATE_FORMAT
import com.eventersapp.consumer.util.AppUtils
import com.eventersapp.consumer.util.AppUtils.base64StringToImage
import com.eventersapp.consumer.util.show


class CustomAdapterMyEvent : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ITEM = 1
        private const val TYPE_LOADING = 2
        private const val ACTIVE = "ACTIVE"
    }

    private var myEventList = ArrayList<MyEventListResponse.Data?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ITEM -> {
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_my_event, parent, false)
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
                myEventList[position]?.let { holder.bindItems(it) }
            }
            is LoadingViewHolder -> {
                holder.showLoadingView()
            }
        }
    }

    override fun getItemCount(): Int {
        return myEventList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            myEventList[position] == null -> TYPE_LOADING
            else -> TYPE_ITEM
        }
    }

    fun setData(newMyEvent: ArrayList<MyEventListResponse.Data?>?) {
        if (newMyEvent != null) {
            if (myEventList.isNotEmpty())
                myEventList.removeAt(myEventList.size - 1)
            myEventList.clear()
            myEventList.addAll(newMyEvent)
        } else {
            myEventList.add(newMyEvent)
        }
        notifyDataSetChanged()
    }

    fun getListData() = myEventList

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textEventIdDateTime: TextView =
            itemView.findViewById(R.id.text_event_id_date_time)
        private val textEventTitle: TextView = itemView.findViewById(R.id.text_event_title)
        private val textEventDescription: TextView =
            itemView.findViewById(R.id.text_event_description)
        private val textTicketPrice: TextView = itemView.findViewById(R.id.text_ticket_price)
        private val textStatus: TextView = itemView.findViewById(R.id.text_status)
        private val imageEventImage: ImageView = itemView.findViewById(R.id.image_event_image)

        @SuppressLint("SetTextI18n")
        fun bindItems(myEvent: MyEventListResponse.Data) {
            textEventIdDateTime.text =
                "#${myEvent.eventTicket.eventTicketId} | ${AppUtils.changeDateFormat(
                    myEvent.publicEvent.dateTime,
                    DISPLAY_DATE_FORMAT,
                    API_DATE_FORMAT
                )}"
            textTicketPrice.text = "$ ${myEvent.eventTicket.price}"
            textEventTitle.text = myEvent.publicEvent.eventTitle
            textEventDescription.text =
                myEvent.publicEvent.eventDescription
            if (myEvent.eventTicket.status == ACTIVE) {
                textStatus.setTextColor(Color.parseColor("#4CAF50"))
            } else {
                textStatus.setTextColor(Color.parseColor("#FF9800"))
            }
            textStatus.text = myEvent.eventTicket.status
            if (myEvent.publicEvent.eventImage != null) {
                imageEventImage.visibility = View.VISIBLE
                imageEventImage.setImageBitmap(base64StringToImage(myEvent.publicEvent.eventImage))
            } else {
                imageEventImage.visibility = View.GONE
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