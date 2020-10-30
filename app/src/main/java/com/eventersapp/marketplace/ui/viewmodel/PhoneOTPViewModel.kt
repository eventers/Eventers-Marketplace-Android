package com.eventersapp.marketplace.ui.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eventersapp.marketplace.data.model.ConnectPostBody
import com.eventersapp.marketplace.data.model.ConnectResponse
import com.eventersapp.marketplace.data.model.VerifiedNumberPostBody
import com.eventersapp.marketplace.data.model.VerifiedNumberResponse
import com.eventersapp.marketplace.data.repositories.PhoneOTPRepository
import com.eventersapp.marketplace.util.*
import com.eventersapp.marketplace.util.AppConstants.FACEBOOK
import com.eventersapp.marketplace.util.AppConstants.GOOGLE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhoneOTPViewModel(private val repository: PhoneOTPRepository) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    var otpDigit1: String? = null
    var otpDigit2: String? = null
    var otpDigit3: String? = null
    var otpDigit4: String? = null
    var otpDigit5: String? = null
    var otpDigit6: String? = null
    private var deviceId = ""
    private var deviceName = ""
    private var opt = ""
    private var provider = ""
    private var userId = -1
    lateinit var phoneNumber: Pair<String, String>
    private var firebaseToken = ""
    private var firebaseUserToken = ""
    private var connectPostBody: ConnectPostBody = ConnectPostBody()
    private var verifiedNumberPostBody: VerifiedNumberPostBody = VerifiedNumberPostBody()

    private val _messageLiveData = MutableLiveData<Event<String>>()
    val messageLiveData: LiveData<Event<String>>
        get() = _messageLiveData

    val otpStatusLiveData: LiveData<Event<State<String>>>
        get() = repository.verificationIdLiveData

    val otpVerificationStatusLiveData: LiveData<Event<State<String>>>
        get() = repository.phoneAuthLiveData

    private val _connectLiveData =
        MutableLiveData<Event<State<ConnectResponse>>>()
    val connectLiveData: LiveData<Event<State<ConnectResponse>>>
        get() = _connectLiveData

    private val _verifiedNumberLiveData =
        MutableLiveData<Event<State<VerifiedNumberResponse>>>()
    val verifiedNumberLiveData: LiveData<Event<State<VerifiedNumberResponse>>>
        get() = _verifiedNumberLiveData

    private lateinit var connectResponse: ConnectResponse
    private lateinit var verifiedNumberResponse: VerifiedNumberResponse

    init {
        getFCMToken()
    }

    fun onSubmitButtonClick(view: View) {
        when {
            otpDigit1.isNullOrEmpty() -> {
                _messageLiveData.postValue(Event("OTP fields should not be empty!"))
                return
            }
            otpDigit2.isNullOrEmpty() -> {
                _messageLiveData.postValue(Event("OTP fields should not be empty!"))
                return
            }
            otpDigit3.isNullOrEmpty() -> {
                _messageLiveData.postValue(Event("OTP fields should not be empty!"))
                return
            }
            otpDigit4.isNullOrEmpty() -> {
                _messageLiveData.postValue(Event("OTP fields should not be empty!"))
                return
            }
            otpDigit5.isNullOrEmpty() -> {
                _messageLiveData.postValue(Event("OTP fields should not be empty!"))
                return
            }
            otpDigit6.isNullOrEmpty() -> {
                _messageLiveData.postValue(Event("OTP fields should not be empty!"))
                return
            }
        }
        opt = otpDigit1 + otpDigit2 + otpDigit3 + otpDigit4 + otpDigit5 + otpDigit6
        verifyOTPCode(opt)

    }

    private fun connect() {
        connectPostBody = createConnectPostBodyJson()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                connectResponse = repository.connect(connectPostBody)
                withContext(Dispatchers.Main) {
                    _connectLiveData.postValue(
                        Event(
                            State.success(
                                connectResponse
                            )
                        )
                    )

                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    _connectLiveData.postValue(Event(State.error(e.message!!)))
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _connectLiveData.postValue(Event(State.error(e.message!!)))
                }
            }
        }
    }

    private fun verifiedNumber() {
        verifiedNumberPostBody = createVerifiedNumberPostBodyJson()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                verifiedNumberResponse = repository.verifiedNumber(verifiedNumberPostBody)
                withContext(Dispatchers.Main) {
                    _verifiedNumberLiveData.postValue(
                        Event(
                            State.success(
                                verifiedNumberResponse
                            )
                        )
                    )

                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    _verifiedNumberLiveData.postValue(Event(State.error(e.message!!)))
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _verifiedNumberLiveData.postValue(Event(State.error(e.message!!)))
                }
            }
        }
    }

    fun onResendOTPButtonClick(view: View) {
        resendVerificationCode()
    }

    fun sendVerificationCode(number: Pair<String, String>) {
        phoneNumber = number
        repository.firebasePhoneAuthentication((phoneNumber.first + phoneNumber.second))
    }

    private fun resendVerificationCode() {
        repository.resendVerificationCode((phoneNumber.first + phoneNumber.second))
    }

    private fun verifyOTPCode(code: String) {
        repository.verifyCode(code)
    }

    private fun createConnectPostBodyJson(): ConnectPostBody {
        provider = AppUtils.getFirebaseProvider(auth)
        val authen = ConnectPostBody().Auth()
        authen.let {
            it.deviceId = deviceId
            it.tokenId = firebaseUserToken
            it.fcmToken = firebaseToken
            it.deviceProvider = deviceName
        }
        val user = ConnectPostBody().User()
        user.let {
            it.provider = provider
            it.phoneNumber = phoneNumber.second
            it.phoneCountryCode = phoneNumber.first
            it.phoneFirebaseId = auth.currentUser?.uid.toString()
        }
        val data = ConnectPostBody().Data()
        data.auth = authen
        data.user = user
        connectPostBody.data = data
        return connectPostBody
    }

    private fun createVerifiedNumberPostBodyJson(): VerifiedNumberPostBody {
        val authen = VerifiedNumberPostBody().Auth()
        authen.let {
            it.deviceId = deviceId
            it.tokenId = firebaseUserToken
            it.fcmToken = firebaseToken
            it.deviceProvider = deviceName
        }
        val user = VerifiedNumberPostBody().User()
        user.let {
            it.provider = provider
            it.phoneNumber = phoneNumber.second
            it.phoneCountryCode = phoneNumber.first
            it.phoneFirebaseId = auth.currentUser?.uid.toString()
            it.userId = userId
        }
        val data = VerifiedNumberPostBody().Data()
        data.auth = authen
        data.user = user
        verifiedNumberPostBody.data = data
        return verifiedNumberPostBody
    }

    private fun getFCMToken() {
        FirebaseInstanceId.getInstance()
            .instanceId.addOnSuccessListener { instanceIdResult: InstanceIdResult ->
                firebaseToken = instanceIdResult.token
                Log.i("Info", "Firebase Token = $firebaseToken")
            }
    }

    fun getJWTToken(firebaseProvider: String, id: Int) {
        provider = firebaseProvider
        userId = id
        viewModelScope.launch(Dispatchers.IO) {
            firebaseUserToken = getFirebaseUserToken()
            withContext(Dispatchers.Main) {
                if (provider == GOOGLE || provider == FACEBOOK)
                    verifiedNumber()
                else
                    connect()
            }
        }
    }

    fun setDeviceInfo(id: String, modelName: String) {
        deviceId = id
        deviceName = modelName
    }

}