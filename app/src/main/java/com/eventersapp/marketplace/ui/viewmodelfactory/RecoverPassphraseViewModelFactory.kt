package com.eventersapp.marketplace.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eventersapp.marketplace.data.repositories.RecoverPassphraseRepository
import com.eventersapp.marketplace.ui.viewmodel.RecoverPassphraseViewModel

@Suppress("UNCHECKED_CAST")
class RecoverPassphraseViewModelFactory(
    private val repository: RecoverPassphraseRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RecoverPassphraseViewModel(repository) as T
    }
}