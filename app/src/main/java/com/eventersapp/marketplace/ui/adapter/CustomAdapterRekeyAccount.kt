package com.eventersapp.marketplace.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.data.model.Account
import com.eventersapp.marketplace.databinding.ListItemRekeyAccountBinding

class CustomAdapterRekeyAccount :
    ListAdapter<Account, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding: ListItemRekeyAccountBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.list_item_rekey_account, parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder -> {
                holder.bindItems(getItem(position))
            }
        }
    }


    inner class ItemViewHolder(val binding: ListItemRekeyAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindItems(myAccount: Account) {
            binding.apply {
                textAccountName.text = myAccount.name
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Account>() {
            override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean =
                oldItem == newItem
        }
    }


}