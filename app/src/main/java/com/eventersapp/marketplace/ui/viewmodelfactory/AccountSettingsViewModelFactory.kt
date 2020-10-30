package com.eventersapp.marketplace.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eventersapp.marketplace.data.repositories.AccountSettingsRepository
import com.eventersapp.marketplace.ui.viewmodel.AccountSettingsViewModel

@Suppress("UNCHECKED_CAST")
class AccountSettingsViewModelFactory(
    private val repository: AccountSettingsRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AccountSettingsViewModel(repository) as T
    }
}