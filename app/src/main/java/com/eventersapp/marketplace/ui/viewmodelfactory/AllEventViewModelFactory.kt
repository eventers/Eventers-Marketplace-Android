package com.eventersapp.marketplace.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eventersapp.marketplace.data.repositories.AllEventRepository
import com.eventersapp.marketplace.ui.viewmodel.AllEventViewModel

@Suppress("UNCHECKED_CAST")
class AllEventViewModelFactory(
    private val repository: AllEventRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AllEventViewModel(repository) as T
    }
}