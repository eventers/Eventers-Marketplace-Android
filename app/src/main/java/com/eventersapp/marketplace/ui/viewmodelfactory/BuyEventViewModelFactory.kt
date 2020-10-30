package com.eventersapp.marketplace.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eventersapp.marketplace.data.repositories.BuyEventRepository
import com.eventersapp.marketplace.ui.viewmodel.BuyEventViewModel

@Suppress("UNCHECKED_CAST")
class BuyEventViewModelFactory(
    private val repository: BuyEventRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BuyEventViewModel(repository) as T
    }
}