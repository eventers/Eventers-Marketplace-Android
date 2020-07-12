package com.eventersapp.consumer.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eventersapp.consumer.data.repositories.MyEventRepository
import com.eventersapp.consumer.ui.viewmodel.MyEventViewModel

@Suppress("UNCHECKED_CAST")
class MyEventViewModelFactory(
    private val repository: MyEventRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MyEventViewModel(repository) as T
    }
}