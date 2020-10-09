package com.eventersapp.marketplace.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.databinding.ListItemMnemonicBinding
import com.eventersapp.marketplace.databinding.ListItemPassphraseBinding

class CustomAdapterVerifyRecoveryPhrase :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val myMnemonicList = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding: ListItemMnemonicBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.list_item_mnemonic, parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder -> {
                myMnemonicList[position].let { holder.bindItems(it) }
            }
        }
    }

    override fun getItemCount(): Int {
        return myMnemonicList.size
    }


    fun setData(newMyMnemonic: ArrayList<String>) {
        myMnemonicList.clear()
        myMnemonicList.addAll(newMyMnemonic)
        notifyDataSetChanged()
    }

    fun getList() = myMnemonicList

    inner class ItemViewHolder(val binding: ListItemMnemonicBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindItems(myMnemonic: String) {
            binding.apply {
                textMnemonic.text = myMnemonic
            }
        }

    }


}