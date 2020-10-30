package com.eventersapp.marketplace.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eventersapp.marketplace.data.model.ConnectPostBody
import com.eventersapp.marketplace.data.model.ConnectResponse
import com.eventersapp.marketplace.data.model.VerifiedNumberPostBody
import com.eventersapp.marketplace.data.model.VerifiedNumberResponse
import com.eventersapp.marketplace.data.network.ApiInterface
import com.eventersapp.marketplace.data.network.SafeApiRequest
import com.eventersapp.marketplace.util.Event
import com.eventersapp.marketplace.util.State
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class PhoneOTPRepository(
    private val api: ApiInterface
) : SafeApiRequest() {

    companion object {
        private const val TIMEOUT_SECONDS = 60L
    }

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var verificationId = ""
    private lateinit var token: PhoneAuthProvider.ForceResendingToken

    private val _verificationIdLiveData =
        MutableLiveData<Event<State<String>>>()
    val verificationIdLiveData: LiveData<Event<State<String>>>
        get() = _verificationIdLiveData
    private val _phoneAuthLiveData =
        MutableLiveData<Event<State<String>>>()
    val phoneAuthLiveData: LiveData<Event<State<String>>>
        get() = _phoneAuthLiveData

    fun firebasePhoneAuthentication(phoneNumber: String) {
        _verificationIdLiveData.postValue(Event(State.loading()))
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            mCallBack
        )
    }

    fun resendVerificationCode(
        phoneNumber: String
    ) {
        _verificationIdLiveData.postValue(Event(State.loading()))
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            TIMEOUT_SECONDS,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            mCallBack,
            token
        )
    }

    private val mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(
                s: String,
                forceResendingToken: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(s, forceResendingToken)
                verificationId = s
                token = forceResendingToken
                _verificationIdLiveData.postValue(Event(State.success(s)))
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode
                if (code != null) {
                    //verifyCode(code)
                } else {
                    signInWithCredential(phoneAuthCredential)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                _verificationIdLiveData.postValue(Event(State.error(e.message ?: "")))
            }
        }

    fun verifyCode(code: String) {
        _phoneAuthLiveData.postValue(Event(State.loading()))
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _phoneAuthLiveData.postValue(Event(State.success(task.result.toString())))
                } else {
                    _phoneAuthLiveData.postValue(Event(State.error(task.exception?.message ?: "")))
                }
            }
    }

    suspend fun connect(
        connectPostBody: ConnectPostBody
    ): ConnectResponse {

        return apiRequest { api.connectData(connectPostBody) }
    }

    suspend fun verifiedNumber(
        verifiedNumberPostBody: VerifiedNumberPostBody
    ): VerifiedNumberResponse {

        return apiRequest { api.verifiedNumberData(verifiedNumberPostBody) }
    }

}