package com.eventersapp.marketplace.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algorand.algosdk.algod.client.model.TransactionID
import com.eventersapp.marketplace.data.model.Account
import com.eventersapp.marketplace.data.repositories.AccountSettingsRepository
import com.eventersapp.marketplace.ui.rekeyaccount.SubmitTx
import com.eventersapp.marketplace.util.Event
import com.eventersapp.marketplace.util.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountSettingsViewModel(private val repository: AccountSettingsRepository) : ViewModel() {

    private var deleteAccountStatus = 0
    private var rekeyAccountAdapterPosition = 0

    private val _deleteAccountStatusLiveData =
        MutableLiveData<Event<Int>>()
    val deleteAccountStatusLiveData: LiveData<Event<Int>>
        get() = _deleteAccountStatusLiveData

    private val _rekeyAccountLiveData =
        MutableLiveData<Event<State<TransactionID>>>()
    val rekeyAccountLiveData: LiveData<Event<State<TransactionID>>>
        get() = _rekeyAccountLiveData

    private val _updateRekeyAccountLiveData =
        MutableLiveData<Event<State<Int>>>()
    val updateRekeyAccountLiveData: LiveData<Event<State<Int>>>
        get() = _updateRekeyAccountLiveData

    private val _accountListLiveData =
        MutableLiveData<ArrayList<Account>>()
    val accountListLiveData: LiveData<ArrayList<Account>>
        get() = _accountListLiveData

    private val _rekeyAccountListLiveData =
        MutableLiveData<Event<ArrayList<Account>>>()
    val rekeyAccountListLiveData: LiveData<Event<ArrayList<Account>>>
        get() = _rekeyAccountListLiveData

    private var accountList = ArrayList<Account>()

    fun fetchAccounts() {
        viewModelScope.launch(Dispatchers.IO) {
            accountList = repository.fetchAccounts() as ArrayList<Account>
            withContext(Dispatchers.Main) {
                _accountListLiveData.postValue(accountList)
            }
        }
    }

    fun fetchRekeyAccounts(myAccount: Account) {
        viewModelScope.launch(Dispatchers.IO) {
            accountList = repository.fetchAccounts() as ArrayList<Account>
            withContext(Dispatchers.Main) {
                accountList.removeAt(0)
                var i = 0
                while (i < accountList.size) {
                    if (myAccount.passphrase == accountList[i].passphrase) {
                        accountList.removeAt(i)
                        i--
                    }
                    i++
                }
                _rekeyAccountListLiveData.postValue(Event(accountList))
            }
        }
    }

    fun updateAccountAddressDetail(
        id: Int?,
        name: String?,
        accountAddress: String?,
        passphrase: String?,
        isSelected: Boolean?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val account = Account(name, accountAddress, passphrase, isSelected)
            account.id = id ?: 0
            repository.updateAccountAddressDetail(
                account
            )
            withContext(Dispatchers.Main) {
                fetchAccounts()
            }
        }
    }

    fun updateAccountDetail(
        id: Int?,
        name: String?,
        accountAddress: String?,
        passphrase: String?,
        isSelected: Boolean?
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

    fun updateRekeyAccountDetail(
        id: Int?,
        name: String?,
        accountAddress: String?,
        passphrase: String?,
        isSelected: Boolean?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val account = Account(name, accountAddress, passphrase, isSelected)
            account.id = id ?: 0
            val updateStatus = repository.updateAccountDetail(
                account
            )
            withContext(Dispatchers.Main) {
                _updateRekeyAccountLiveData.postValue(Event(State.success(updateStatus)))
            }
        }
    }

    fun deleteAccount(
        id: Int?,
        name: String?,
        accountAddress: String?,
        passphrase: String?,
        isSelected: Boolean?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val account = Account(name, accountAddress, passphrase, isSelected)
            account.id = id ?: 0
            deleteAccountStatus = repository.deleteAccount(
                account
            )
            withContext(Dispatchers.Main) {
                _deleteAccountStatusLiveData.postValue(Event(deleteAccountStatus))
            }
        }
    }

    /**
    passphrase is of account that wants to rekey means source account e.g Account 1
    address is of destination account e.g Account 2
    Account 1 is rekey to Account 2
     */

    fun rekeyAccount(passphrase: String?, address: String?) {
        _rekeyAccountLiveData.postValue(Event(State.loading()))
        viewModelScope.launch(Dispatchers.IO) {
            val transactionId = SubmitTx.rekeyAccount(
                passphrase,
                address
            )
            withContext(Dispatchers.Main) {
                if (transactionId == null)
                    _rekeyAccountLiveData.postValue(Event(State.error("Error! Please add balance for transaction")))
                else
                    _rekeyAccountLiveData.postValue(Event(State.success(transactionId)))
            }
        }

    }

    fun setRekeyAccountAdapterPosition(position: Int) {
        rekeyAccountAdapterPosition = position
    }

    fun getRekeyAccountAdapterPosition() = rekeyAccountAdapterPosition
}