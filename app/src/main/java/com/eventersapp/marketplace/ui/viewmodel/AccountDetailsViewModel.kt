package com.eventersapp.marketplace.ui.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.data.model.Account
import com.eventersapp.marketplace.data.repositories.AccountDetailsRepository
import com.eventersapp.marketplace.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountDetailsViewModel(private val repository: AccountDetailsRepository) : ViewModel() {

    var accountName = MutableLiveData<String>()

    private var accountAddress = ""
    private var passphrase = ""

    private val _messageLiveData = MutableLiveData<Event<String>>()
    val messageLiveData: LiveData<Event<String>>
        get() = _messageLiveData

    fun onNextButtonClick(view: View) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveAccountDetail(
                Account(
                    accountName.value,
                    accountAddress,
                    passphrase,
                    false
                )
            )
            withContext(Dispatchers.Main) {
                _messageLiveData.postValue(Event("Account created successfully"))
                Navigation.findNavController(view)
                    .navigate(R.id.action_accountDetailsFragment_to_accountSettingsFragment)
            }
        }
    }

    fun setAccountAddress(address: String) {
        accountAddress = address
    }

    fun setPassphrase(data: String) {
        passphrase = data
    }
}