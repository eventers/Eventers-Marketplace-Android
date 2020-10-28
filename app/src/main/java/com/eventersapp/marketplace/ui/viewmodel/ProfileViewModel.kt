package com.eventersapp.marketplace.ui.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eventersapp.marketplace.data.model.LogoutPostBody
import com.eventersapp.marketplace.data.model.LogoutResponse
import com.eventersapp.marketplace.data.model.ProfileResponse
import com.eventersapp.marketplace.data.repositories.ProfileRepository
import com.eventersapp.marketplace.util.*
import com.eventersapp.marketplace.util.AppConstants.OFFLINE
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    private var deviceId = ""
    private var userId = -1
    private var firebaseUserToken = ""
    var isProfileApiCalled = false

    private var logoutPostBody: LogoutPostBody = LogoutPostBody()

    private val _logoutLiveData = MutableLiveData<Event<State<LogoutResponse>>>()
    val logoutLiveData: LiveData<Event<State<LogoutResponse>>>
        get() = _logoutLiveData

    private val _profileDetailLiveData = MutableLiveData<State<ProfileResponse>>()
    val profileDetailLiveData: LiveData<State<ProfileResponse>>
        get() = _profileDetailLiveData

    private val _accountAddressLiveData = MutableLiveData<String>()
    val accountAddressLiveData: LiveData<String>
        get() = _accountAddressLiveData

    private lateinit var logoutResponse: LogoutResponse
    private lateinit var profileResponse: ProfileResponse

    private fun getProfileDetail() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                profileResponse = repository.getProfile(userId, firebaseUserToken)
                withContext(Dispatchers.Main) {
                    isProfileApiCalled = true
                    fetchSelectedAddressFromDb()
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
                    isProfileApiCalled = false
                    deleteAllRecordsFromDb()
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

    private fun deleteAllRecordsFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTableRecordsFromDb()
            withContext(Dispatchers.Main) {
                _logoutLiveData.postValue(
                    Event(
                        State.success(
                            logoutResponse
                        )
                    )
                )
            }
        }
    }

    fun fetchSelectedAddressFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            val account = repository.fetchSelectedAddressFromDb()
            withContext(Dispatchers.Main) {
                _accountAddressLiveData.postValue(account.accountAddress)
            }
        }
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

    fun setDeviceInfo(id: String) {
        deviceId = id
    }

    fun getLoadedProfileData() {
        _profileDetailLiveData.postValue(
            State.success(
                profileResponse

            )
        )
    }

}