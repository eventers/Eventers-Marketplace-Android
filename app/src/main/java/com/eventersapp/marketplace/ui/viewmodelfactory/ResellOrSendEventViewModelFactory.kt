package com.eventersapp.marketplace.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eventersapp.marketplace.data.repositories.ResellOrSendEventRepository
import com.eventersapp.marketplace.ui.viewmodel.ResellOrSendEventViewModel

@Suppress("UNCHECKED_CAST")
class ResellOrSendEventViewModelFactory(
    private val repository: ResellOrSendEventRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ResellOrSendEventViewModel(repository) as T
    }
}