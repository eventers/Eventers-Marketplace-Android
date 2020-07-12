package com.eventersapp.consumer.ui.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eventersapp.consumer.data.model.LogoutPostBody
import com.eventersapp.consumer.data.model.LogoutResponse
import com.eventersapp.consumer.data.model.ProfileResponse
import com.eventersapp.consumer.data.repositories.ProfileRepository
import com.eventersapp.consumer.util.*
import com.eventersapp.consumer.util.AppConstants.OFFLINE
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    private var deviceId = ""
    private var userId = -1
    private var firebaseUserToken = ""

    private var logoutPostBody: LogoutPostBody = LogoutPostBody()

    private val _logoutLiveData = MutableLiveData<Event<State<LogoutResponse>>>()
    val logoutLiveData: LiveData<Event<State<LogoutResponse>>>
        get() = _logoutLiveData
    private val _profileDetailLiveData = MutableLiveData<State<ProfileResponse>>()
    val profileDetailLiveData: LiveData<State<ProfileResponse>>
        get() = _profileDetailLiveData

    private lateinit var logoutResponse: LogoutResponse
    private lateinit var profileResponse: ProfileResponse

    private fun getProfileDetail() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                profileResponse = repository.getProfile(userId, firebaseUserToken)
                withContext(Dispatchers.Main) {
                    _profileDetailLiveData.postValue(
                        State.success(
                            profileResponse

                        )
                    )

                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    _profileDetailLiveData.postValue(State.error(e.message!!))
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _profileDetailLiveData.postValue(State.error(e.message!!))
                }
            }
        }
    }

    fun onLogoutButtonClick(view: View) {
        _logoutLiveData.postValue(Event(State.loading()))
        logoutPostBody = createLogoutPostBodyJson()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                logoutResponse = repository.logout(logoutPostBody)
                FirebaseAuth.getInstance().signOut()
                LoginManager.getInstance().logOut()
                withContext(Dispatchers.Main) {
                    _logoutLiveData.postValue(
                        Event(
                            State.success(
                                logoutResponse
                            )
                        )
                    )

                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    _logoutLiveData.postValue(Event(State.error(e.message!!)))
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _logoutLiveData.postValue(Event(State.error(e.message!!)))
                }
            }
        }
    }

    private fun createLogoutPostBodyJson(): LogoutPostBody {
        val authen = LogoutPostBody().Auth()
        authen.let {
            it.deviceId = deviceId
            it.tokenId = firebaseUserToken
            it.authType = OFFLINE
            it.userId = userId

        }
        val userDevice = LogoutPostBody().UserDevice()
        userDevice.isValid = false
        val data = LogoutPostBody().Data()
        data.auth = authen
        data.userDevice = userDevice
        logoutPostBody.data = data
        return logoutPostBody
    }

    fun getJWTToken(id: Int) {
        _profileDetailLiveData.postValue(State.loading())
        userId = id
        viewModelScope.launch(Dispatchers.IO) {
            firebaseUserToken = getFirebaseUserToken()
            withContext(Dispatchers.Main) {
                getProfileDetail()
            }
        }

    }

    fun getDeviceInfo(id: String) {
        deviceId = id
    }
}