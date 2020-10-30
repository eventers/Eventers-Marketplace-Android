package com.eventersapp.marketplace.ui.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.algorand.algosdk.account.Account
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.util.Event
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

class BackupPassphraseViewModel : ViewModel() {

    var question = MutableLiveData<String>()
    var selectPassPhrase = MutableLiveData<String>()
    private var questionCount = 0
    private var randomNumber = 0
    private var passphraseStr = ""
    private var accountAddress = ""

    private val _messageLiveData = MutableLiveData<Event<String>>()
    val messageLiveData: LiveData<Event<String>>
        get() = _messageLiveData

    private val _questionCountLiveData = MutableLiveData<Event<Int>>()
    val questionCountLiveData: LiveData<Event<Int>>
        get() = _questionCountLiveData

    private val _passphraseListLiveData =
        MutableLiveData<ArrayList<String>>()
    val passphraseListLiveData: LiveData<ArrayList<String>>
        get() = _passphraseListLiveData

    private val _randomPassphraseListLiveData =
        MutableLiveData<ArrayList<String>>()
    val randomPassphraseListLiveData: LiveData<ArrayList<String>>
        get() = _randomPassphraseListLiveData

    private val passphrase = ArrayList<String>()
    private val randomPassphrase = ArrayList<String>()

    fun generatePassPhrase() {
        Security.removeProvider("BC")
        Security.insertProviderAt(BouncyCastleProvider(), 0)
        val myAccount = Account()
        accountAddress = myAccount.address.encodeAsString()
        passphraseStr = myAccount.toMnemonic()
        passphrase.clear()
        passphrase.addAll(passphraseStr.split(" ").toList())
        _passphraseListLiveData.postValue(passphrase)
    }

    fun onVerifyRecoveryButtonClick(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_backupPassphraseFragment_to_verifyRecoveryPhraseFragment)
    }

    fun showRandomPassphraseList(count: Int) {
        questionCount = count
        randomPassphrase.clear()
        randomNumber = (0..24).random()
        question.value = "Question $questionCount of 3"
        selectPassPhrase.value = "Select the ${(randomNumber + 1)} word of your passphrase"
        Log.i("Info", "Word = ${passphrase[randomNumber]}")
        randomPassphrase.add(passphrase[randomNumber])
        val uniqueSetList = mutableSetOf<String>()
        while (uniqueSetList.size < 16) { // Change the no. if you want more random words
            val item = (0..24).random()
            if (item != randomNumber)
                uniqueSetList.add(passphrase[item])
        }
        Log.i("Info", "uniqueSetList = $uniqueSetList")
        for (word in uniqueSetList) {
            randomPassphrase.add(word)
        }
        randomPassphrase.shuffle()
        _randomPassphraseListLiveData.postValue(randomPassphrase)
    }

    fun checkPassphraseWord(selectedPassphrase: String) {
        if (selectedPassphrase == passphrase[randomNumber]) {
            _messageLiveData.postValue(Event("Correct choice"))
            questionCount++
            if (questionCount < 4) {
                showRandomPassphraseList(questionCount)
            }
            _questionCountLiveData.postValue(Event(questionCount))
        } else {
            _messageLiveData.postValue(Event("Wrong choice"))
        }
    }

    fun getPassphrase() = passphraseStr

    fun getAccountAddress() = accountAddress
}