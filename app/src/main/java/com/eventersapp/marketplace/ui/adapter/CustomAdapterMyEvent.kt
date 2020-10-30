package com.eventersapp.marketplace.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.data.model.MyEventListResponse
import com.eventersapp.marketplace.databinding.ListItemMyEventBinding
import com.eventersapp.marketplace.util.AppConstants.API_DATE_FORMAT
import com.eventersapp.marketplace.util.AppConstants.DISPLAY_DATE_FORMAT
import com.eventersapp.marketplace.util.AppUtils
import com.eventersapp.marketplace.util.LoadingViewHolder


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
                val binding: ListItemMyEventBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context), R.layout.list_item_my_event, parent,
                    false
                )
                ItemViewHolder(binding)
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

    inner class ItemViewHolder(val binding: ListItemMyEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindItems(myEvent: MyEventListResponse.Data) {
            binding.apply {
                textEventIdDateTime.text =
                    "#${myEvent.eventTicket.eventTicketId} | ${
                        AppUtils.changeDateFormat(
                            myEvent.publicEvent.dateTime,
                            DISPLAY_DATE_FORMAT,
                            API_DATE_FORMAT
                        )
                    }"
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
                if (myEvent.publicEvent.eventImage != null && myEvent.publicEvent.eventImage.isNotEmpty()) {
                    imageEventImage.visibility = View.VISIBLE
                    AppUtils.setGlideImage(imageEventImage, myEvent.publicEvent.eventImage)
                } else {
                    imageEventImage.visibility = View.GONE
                }
            }

        }
    }

}