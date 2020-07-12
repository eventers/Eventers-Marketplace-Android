package com.eventersapp.consumer.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eventersapp.consumer.data.repositories.CreateEventRepository
import com.eventersapp.consumer.ui.viewmodel.CreateEventViewModel

@Suppress("UNCHECKED_CAST")
class CreateEventViewModelFactory(
    private val repository: CreateEventRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CreateEventViewModel(repository) as T
    }
}