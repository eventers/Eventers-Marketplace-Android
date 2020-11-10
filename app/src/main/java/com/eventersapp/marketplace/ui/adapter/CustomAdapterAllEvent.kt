package com.eventersapp.marketplace.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.data.model.AllEventListResponse
import com.eventersapp.marketplace.databinding.ListItemAllEventBinding
import com.eventersapp.marketplace.util.AppConstants
import com.eventersapp.marketplace.util.AppUtils
import com.eventersapp.marketplace.util.LoadingViewHolder

class CustomAdapterAllEvent : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ITEM = 1
        private const val TYPE_LOADING = 2
    }

    private var allEventList = ArrayList<AllEventListResponse.Data?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ITEM -> {
                val binding: ListItemAllEventBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context), R.layout.list_item_all_event, parent,
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
            allEventList.clear()
            allEventList.addAll(newAllEvent)
        } else {
            allEventList.add(newAllEvent)
        }
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(val binding: ListItemAllEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindItems(allEvent: AllEventListResponse.Data) {
            binding.apply {
                allEvent.publicEvent.let {
                    textEventTitle.text = it.eventTitle
                    textEventDescription.text = it.eventDescription
                    textDateTime.text = AppUtils.changeDateFormat(
                        it.dateTime,
                        AppConstants.DISPLAY_DATE_FORMAT,
                        AppConstants.API_DATE_FORMAT
                    )
                    textTicketPrice.text = "$ ${it.ticketPrice}"
                    if (it.eventImage != null && it.eventImage.isNotEmpty()) {
                        imageEventImage.visibility = View.VISIBLE
                        AppUtils.setGlideImage(imageEventImage, it.eventImage)
                    } else {
                        imageEventImage.visibility = View.GONE
                    }
                }
            }

        }

    }

}