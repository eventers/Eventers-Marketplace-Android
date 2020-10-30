package com.eventersapp.marketplace.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eventersapp.marketplace.data.repositories.PhoneOTPRepository
import com.eventersapp.marketplace.ui.viewmodel.PhoneOTPViewModel

@Suppress("UNCHECKED_CAST")
class PhoneOTPViewModelFactory(
    private val repository: PhoneOTPRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PhoneOTPViewModel(repository) as T
    }
}