package com.eventersapp.consumer.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eventersapp.consumer.data.repositories.ResellOrSendEventRepository
import com.eventersapp.consumer.ui.viewmodel.ResellOrSendEventViewModel

@Suppress("UNCHECKED_CAST")
class ResellOrSendEventViewModelFactory(
    private val repository: ResellOrSendEventRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ResellOrSendEventViewModel(repository) as T
    }
}