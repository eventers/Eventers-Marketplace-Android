package com.eventersapp.consumer.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eventersapp.consumer.R
import com.eventersapp.consumer.data.model.AllEventListResponse
import com.eventersapp.consumer.ui.blockchain.BuyEventFragment
import com.eventersapp.consumer.util.AppConstants
import com.eventersapp.consumer.util.show

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
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_buy_event, parent, false)
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
            if (buyEventList.isNotEmpty())
                buyEventList.removeAt(buyEventList.size - 1)
            buyEventList.clear()
            buyEventList.addAll(newBuyEvent)
        } else {
            buyEventList.add(newBuyEvent)
        }
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val textTicketPrice: TextView = itemView.findViewById(R.id.text_ticket_price)
        private val textBuyTicket: TextView = itemView.findViewById(R.id.text_buy_ticket)

        @SuppressLint("SetTextI18n")
        fun bindItems(buyEvent: AllEventListResponse.Data.EventTicket) {
            textTicketPrice.text = "$ ${buyEvent.price}"
            textBuyTicket.setOnClickListener {
                fragment.showPayNowDialog(
                    buyEvent.eventTicketId,
                    AppConstants.RESELL_BUY_TICKET
                )
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