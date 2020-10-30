package com.eventersapp.marketplace.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eventersapp.marketplace.data.repositories.BlogRepository
import com.eventersapp.marketplace.ui.viewmodel.BlogViewModel

@Suppress("UNCHECKED_CAST")
class BlogViewModelFactory(
    private val repository: BlogRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BlogViewModel(repository) as T
    }
}