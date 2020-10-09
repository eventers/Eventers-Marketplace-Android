package com.eventersapp.marketplace.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eventersapp.marketplace.data.model.ConnectPostBody
import com.eventersapp.marketplace.data.model.ConnectResponse
import com.eventersapp.marketplace.data.repositories.SignupRepository
import com.eventersapp.marketplace.util.*
import com.eventersapp.marketplace.util.AppConstants.FACEBOOK
import com.eventersapp.marketplace.util.AppConstants.GOOGLE
import com.facebook.AccessToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignupViewModel(private val repository: SignupRepository) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var deviceId = ""
    private var deviceName = ""
    private var firebaseToken = ""
    private var firebaseUserToken = ""
    private var provider = ""
    private var connectPostBody: ConnectPostBody = ConnectPostBody()

    val googleSignLiveData: LiveData<Event<State<String>>>
        get() = repository.googleUserLiveData

    val facebookSignLiveData: LiveData<Event<State<String>>>
        get() = repository.facebookUserLiveData

    private val _connectLiveData =
        MutableLiveData<Event<State<ConnectResponse>>>()
    val connectLiveData: LiveData<Event<State<ConnectResponse>>>
        get() = _connectLiveData
    private lateinit var connectResponse: ConnectResponse

    init {
        getFCMToken()
    }

    fun firebaseAuthWithGoogle(id: String) {
        repository.firebaseAuthWithGoogle(id)
    }


    fun firebaseAuthWithFacebook(token: AccessToken) {
        repository.firebaseAuthWithFacebook(token)
    }

    fun getJWTToken() {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseUserToken = getFirebaseUserToken()
            withContext(Dispatchers.Main) {
                connect()
            }
        }

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
            if (provider == GOOGLE) {
                it.gEmail = auth.currentUser?.email
                it.gImageUrl = auth.currentUser?.photoUrl?.toString()!!.replace("s96-c", "s400-c")
                it.gName = auth.currentUser?.displayName
                it.gFirebaseId = auth.currentUser?.uid.toString()
            } else if (provider == FACEBOOK) {
                it.fbEmail = auth.currentUser?.email
                it.fbImageUrl = auth.currentUser?.photoUrl?.toString() + "?width=400"
                it.fbName = auth.currentUser?.displayName
                it.fbFirebaseId = auth.currentUser?.uid.toString()
            }
        }
        val data = ConnectPostBody().Data()
        data.auth = authen
        data.user = user
        connectPostBody.data = data
        return connectPostBody
    }

    private fun getFCMToken() {
        FirebaseInstanceId.getInstance()
            .instanceId.addOnSuccessListener { instanceIdResult: InstanceIdResult ->
                firebaseToken = instanceIdResult.token
                Log.i("Info", "Firebase Token = $firebaseToken")
            }
    }

    fun setDeviceInfo(id: String, modelName: String) {
        deviceId = id
        deviceName = modelName
    }

}