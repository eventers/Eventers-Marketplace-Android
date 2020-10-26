package com.eventersapp.marketplace.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.data.model.Account
import com.eventersapp.marketplace.databinding.ListItemAccountBinding
import com.eventersapp.marketplace.ui.viewmodel.AccountSettingsViewModel
import com.eventersapp.marketplace.util.hide
import com.eventersapp.marketplace.util.show

class CustomAdapterAccount(private val viewModel: AccountSettingsViewModel) :
    ListAdapter<Account, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding: ListItemAccountBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.list_item_account, parent,
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

    inner class ItemViewHolder(val binding: ListItemAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindItems(myAccount: Account) {
            binding.apply {
                if(adapterPosition == 0)
                    imageMoreActions.hide()
                else
                    imageMoreActions.show()
                textAccountName.text = myAccount.name
                myAccount.isSelected?.let {
                    radioButtonSelectAccount.isChecked = it
                }
                radioButtonSelectAccount.setOnClickListener {
                    viewModel.updateAccountDetail(
                        myAccount.id,
                        myAccount.name,
                        myAccount.accountAddress,
                        myAccount.passphrase,
                        true
                    )
                }
                imageMoreActions.setOnClickListener {
                    Navigation.findNavController(it).navigate(R.id.action_accountSettingsFragment_to_accountSettingsMoreOptionBottomDialogFragment,
                    bundleOf("account_detail" to myAccount))
                }
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