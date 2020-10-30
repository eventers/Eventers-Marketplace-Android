package com.eventersapp.marketplace.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eventersapp.marketplace.data.repositories.MyEventRepository
import com.eventersapp.marketplace.ui.viewmodel.MyEventViewModel

@Suppress("UNCHECKED_CAST")
class MyEventViewModelFactory(
    private val repository: MyEventRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MyEventViewModel(repository) as T
    }
}