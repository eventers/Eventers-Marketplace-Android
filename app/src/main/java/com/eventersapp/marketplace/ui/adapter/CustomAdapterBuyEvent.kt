package com.eventersapp.marketplace.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.data.model.AllEventListResponse
import com.eventersapp.marketplace.databinding.ListItemBuyEventBinding
import com.eventersapp.marketplace.ui.blockchain.BuyEventFragment
import com.eventersapp.marketplace.util.AppConstants
import com.eventersapp.marketplace.util.LoadingViewHolder

class CustomAdapterBuyEvent(private val fragment: BuyEventFragment) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ITEM = 1
        private const val TYPE_LOADING = 2
    }

    private var buyEventList = ArrayList<AllEventListResponse.Data.EventTicket>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ITEM -> {
                val binding: ListItemBuyEventBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context), R.layout.list_item_buy_event, parent,
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
                buyEventList[position].let { holder.bindItems(it) }
            }
            is LoadingViewHolder -> {
                holder.showLoadingView()
            }
        }
    }

    override fun getItemCount(): Int {
        return buyEventList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            buyEventList[position] == null -> TYPE_LOADING
            else -> TYPE_ITEM
        }
    }

    fun setData(newBuyEvent: ArrayList<AllEventListResponse.Data.EventTicket>) {
        if (newBuyEvent != null) {
            buyEventList.clear()
            buyEventList.addAll(newBuyEvent)
        } else {
            buyEventList.add(newBuyEvent)
        }
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(val binding: ListItemBuyEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindItems(buyEvent: AllEventListResponse.Data.EventTicket) {
            binding.apply {
                textTicketPrice.text = "$ ${buyEvent.price}"
                textBuyTicket.setOnClickListener {
                    fragment.showPayNowDialog(
                        buyEvent.eventTicketId,
                        AppConstants.RESELL_BUY_TICKET
                    )
                }
            }

        }

    }

}