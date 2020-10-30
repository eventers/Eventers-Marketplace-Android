package com.eventersapp.marketplace.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eventersapp.marketplace.data.repositories.SignupRepository
import com.eventersapp.marketplace.ui.viewmodel.SignupViewModel

@Suppress("UNCHECKED_CAST")
class SignupViewModelFactory(
    private val repository: SignupRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SignupViewModel(repository) as T
    }
}