package com.eventersapp.marketplace.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.databinding.ListItemPassphraseBinding

class CustomAdapterBackupPassphrase :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val myPassphraseList = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding: ListItemPassphraseBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.list_item_passphrase, parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder -> {
                myPassphraseList[position].let { holder.bindItems(it) }
            }
        }
    }

    override fun getItemCount(): Int {
        return myPassphraseList.size
    }


    fun setData(newMyPassphrase: ArrayList<String>) {
        myPassphraseList.clear()
        myPassphraseList.addAll(newMyPassphrase)
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(val binding: ListItemPassphraseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindItems(myPassphrase: String) {
            val number = "${(adapterPosition + 1)}."
            val spannableStr = SpannableString(number + myPassphrase)
            spannableStr.setSpan(
                ForegroundColorSpan(Color.GRAY),
                0,
                number.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.apply {
                textPassphrase.text = spannableStr
            }
        }

    }


}