package com.eventersapp.consumer.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eventersapp.consumer.data.repositories.AllEventRepository
import com.eventersapp.consumer.ui.viewmodel.AllEventViewModel

@Suppress("UNCHECKED_CAST")
class AllEventViewModelFactory(
    private val repository: AllEventRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AllEventViewModel(repository) as T
    }
}