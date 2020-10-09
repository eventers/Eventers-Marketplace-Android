package com.eventersapp.marketplace.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eventersapp.marketplace.data.model.Account
import com.eventersapp.marketplace.data.repositories.AccountSettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountSettingsViewModel(private val repository: AccountSettingsRepository) : ViewModel() {

    private val _accountListLiveData =
        MutableLiveData<ArrayList<Account>>()
    val accountListLiveData: LiveData<ArrayList<Account>>
        get() = _accountListLiveData

    private var accountList = ArrayList<Account>()

   fun fetchAccounts() {
        viewModelScope.launch(Dispatchers.IO) {
            accountList = repository.fetchAccounts() as ArrayList<Account>
            withContext(Dispatchers.Main) {
                _accountListLiveData.postValue(accountList)
            }
        }
    }

    fun updateAccountDetail(
        id: Int?,
        name: String?,
        accountAddress: String?,
        passphrase: String?,
        isSelected: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val account = Account(name, accountAddress, passphrase, isSelected)
            account.id = id ?: 0
            repository.updateAccountDetail(
                account
            )
            withContext(Dispatchers.Main) {
                fetchAccounts()
            }
        }
    }
}