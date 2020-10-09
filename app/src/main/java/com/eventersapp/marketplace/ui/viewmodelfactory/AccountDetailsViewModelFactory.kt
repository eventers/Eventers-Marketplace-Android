package com.eventersapp.marketplace.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eventersapp.marketplace.data.repositories.AccountDetailsRepository
import com.eventersapp.marketplace.data.repositories.BuyEventRepository
import com.eventersapp.marketplace.ui.viewmodel.AccountDetailsViewModel
import com.eventersapp.marketplace.ui.viewmodel.BuyEventViewModel

@Suppress("UNCHECKED_CAST")
class AccountDetailsViewModelFactory(
    private val repository: AccountDetailsRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AccountDetailsViewModel(repository) as T
    }
}