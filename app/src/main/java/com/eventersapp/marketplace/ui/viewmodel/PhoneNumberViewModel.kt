package com.eventersapp.marketplace.ui.viewmodel

import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.eventersapp.marketplace.R
import com.eventersapp.marketplace.util.Event

class PhoneNumberViewModel : ViewModel() {

    var phoneNumber: String? = null
    var provider = ""
    private var countryCode: String? = null
    var checkedValuePrivacyPolicy: Boolean = false

    private val _messageLiveData = MutableLiveData<Event<String>>()
    val messageLiveData: LiveData<Event<String>>
        get() = _messageLiveData

    init {
        countryCode = "+91"
    }

    fun onSubmitButtonClick(view: View) {
        when {
            phoneNumber.isNullOrEmpty() -> {
                _messageLiveData.postValue(Event("Phone number should not be empty!"))
                return
            }
            !checkedValuePrivacyPolicy -> {
                _messageLiveData.postValue(Event("Please accept the terms and conditions!"))
                return
            }
        }
        Navigation.findNavController(view)
            .navigate(
                R.id.action_phoneNumberFragment_to_phoneOTPFragment,
                bundleOf(
                    "phone_number" to phoneNumber,
                    "country_code" to countryCode
                )
            )

    }

    fun setCountryCode(phoneCode: String) {
        countryCode = "+$phoneCode"
    }

}