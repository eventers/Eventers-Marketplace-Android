package com.eventersapp.marketplace.ui.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algorand.algosdk.account.Account
import com.algorand.algosdk.mnemonic.Mnemonic
import com.eventersapp.marketplace.data.repositories.RecoverPassphraseRepository
import com.eventersapp.marketplace.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

class RecoverPassphraseViewModel(private val repository: RecoverPassphraseRepository) :
    ViewModel() {

    var name = MutableLiveData<String>()
    var passphrase = MutableLiveData<String>()

    private val _messageLiveData = MutableLiveData<Event<String>>()
    val messageLiveData: LiveData<Event<String>>
        get() = _messageLiveData

    private val _showErrorDialogLiveData = MutableLiveData<Event<String>>()
    val showErrorDialogLiveData: LiveData<Event<String>>
        get() = _showErrorDialogLiveData

    fun onVerifyButtonClick(view: View) {

        when {
            name.value.isNullOrEmpty() -> {
                _messageLiveData.postValue(Event("Account name should not be empty!"))
                return
            }
            passphrase.value.isNullOrEmpty() -> {
                _messageLiveData.postValue(Event("Passphrase should not be empty!"))
                return
            }
        }
        fetchPassphraseFromDb(passphrase.value ?: "")
    }

    private fun fetchPassphraseFromDb(passphrase: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val account = repository.fetchAccountByPassphrase(passphrase)
            withContext(Dispatchers.Main) {
                if (account?.name != null) {
                    _showErrorDialogLiveData.postValue(Event("This account already exists."))
                } else {
                    recoverAddressFromPassphrase()
                }
            }
        }
    }

    private fun recoverAddressFromPassphrase() {
        Security.removeProvider("BC")
        Security.insertProviderAt(BouncyCastleProvider(), 0)
        try {
            val seed = Mnemonic.toKey(passphrase.value)
            val myAccount = Account(seed)
            saveRecoverAddressToDb(myAccount.address.encodeAsString())
            Log.i("Info", "Recover address = ${myAccount.address}")
        } catch (e: Exception) {
            Log.i("Info", "Exception = $e")
            _showErrorDialogLiveData.postValue(Event("Yor passphrase is invalid. Please make sure that you entered correct one"))
        }

    }

    private fun saveRecoverAddressToDb(accountAddress: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveAccountDetail(
                com.eventersapp.marketplace.data.model.Account(
                    name.value,
                    accountAddress,
                    passphrase.value,
                    false
                )
            )
            withContext(Dispatchers.Main) {
                _messageLiveData.postValue(Event("Account recovered successfully"))
            }
        }
    }
}