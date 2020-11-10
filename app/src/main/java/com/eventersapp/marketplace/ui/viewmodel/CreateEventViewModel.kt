package com.eventersapp.marketplace.ui.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eventersapp.marketplace.data.model.ConnectResponse
import com.eventersapp.marketplace.data.model.CreateEventPostBody
import com.eventersapp.marketplace.data.model.CreateEventResponse
import com.eventersapp.marketplace.data.repositories.CreateEventRepository
import com.eventersapp.marketplace.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CreateEventViewModel(private val repository: CreateEventRepository) : ViewModel() {

    companion object {
        const val ONLINE = "online"
    }

    var eventTitle: String? = null
    var eventDescription: String? = null
    var totalTickets: String? = null
    var ticketPrice: String? = null
    var ticketImage: String? = null

    private var firebaseUserToken = ""
    private var deviceId = ""
    private var filePath = ""
    private var mediaName = ""
    private var mediaPath = ""

    private var connectResponse: ConnectResponse? = null
    private var createEventPostBody: CreateEventPostBody = CreateEventPostBody()

    private val _messageLiveData = MutableLiveData<Event<String>>()
    val messageLiveData: LiveData<Event<String>>
        get() = _messageLiveData
    private val _createEventLiveData =
        MutableLiveData<Event<State<CreateEventResponse>>>()
    val createEventLiveData: LiveData<Event<State<CreateEventResponse>>>
        get() = _createEventLiveData
    private lateinit var createEventResponse: CreateEventResponse

    init {
        mediaPath = "public_event/"
    }

    fun onSubmitButtonClick(view: View) {
        when {
            eventTitle.isNullOrEmpty() -> {
                _messageLiveData.postValue(Event("Event title should not be empty!"))
                return
            }
            eventDescription.isNullOrEmpty() -> {
                _messageLiveData.postValue(Event("Event description should not be empty!"))
                return
            }
            totalTickets.isNullOrEmpty() -> {
                _messageLiveData.postValue(Event("Total tickets should not be empty!"))
                return
            }
            totalTickets!!.toInt() == 0 -> {
                _messageLiveData.postValue(Event("Total tickets should not be zero!"))
                return
            }
            totalTickets!!.toInt() > 499 -> {
                _messageLiveData.postValue(Event("Total tickets should not be less then 500!"))
                return
            }
            ticketPrice.isNullOrEmpty() -> {
                _messageLiveData.postValue(Event("Ticket price should not be empty!"))
                return
            }
            ticketPrice!!.toInt() == 0 -> {
                _messageLiveData.postValue(Event("Ticket price should be greater then zero!"))
                return
            }
            ticketImage.isNullOrEmpty() -> {
                _messageLiveData.postValue(Event("Please select image!"))
                return
            }
        }
        _createEventLiveData.postValue(Event(State.loading()))
        getJWTToken()

    }

    private fun createEvent() {
        createEventPostBody = createEventPostBodyJson()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                createEventResponse = repository.createEvent(createEventPostBody)
                withContext(Dispatchers.Main) {
                    _createEventLiveData.postValue(Event(State.success(createEventResponse)))
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    _createEventLiveData.postValue(Event(State.error(e.message!!)))
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _createEventLiveData.postValue(Event(State.error(e.message!!)))
                }
            }
        }
    }

    fun setUserInfo(
        userData: ConnectResponse?,
        id: String
    ) {
        connectResponse = userData
        deviceId = id
    }

    private fun createEventPostBodyJson(): CreateEventPostBody {
        val authen = CreateEventPostBody().Auth()
        authen.let {
            it.deviceId = deviceId
            it.tokenId = firebaseUserToken
            it.authType = ONLINE
            it.userId = connectResponse?.data?.user?.userId!!.toInt()
            it.pushKey = ""
        }
        val publicEvent = CreateEventPostBody().PublicEvent()
        publicEvent.let {
            it.dateTime = AppUtils.getDateTime()
            it.eventDescription = eventDescription
            it.eventTitle = eventTitle
            it.eventImage =
                if (filePath.isNotEmpty()) AppConstants.S3URL + mediaPath + mediaName else ""
            it.ticketPrice = ticketPrice!!.toInt()
            it.totalTickets = totalTickets!!.toInt()
        }
        val data = CreateEventPostBody().Data()
        data.auth = authen
        data.publicEvent = publicEvent
        createEventPostBody.data = data
        return createEventPostBody
    }

    private fun getJWTToken() {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseUserToken = getFirebaseUserToken()
            withContext(Dispatchers.Main) {
                createEvent()
            }
        }

    }

    fun setFilePath(path: String) {
        filePath = path
        ticketImage = filePath
    }

    fun getFilePath() = filePath

    fun setMediaName(name: String) {
        mediaName = name
        mediaName = AppUtils.getDateTime() + "_" + mediaName
    }

    fun getMediaName() = mediaName

    fun getMediaPath() = mediaPath

}