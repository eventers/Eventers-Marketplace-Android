package com.eventersapp.marketplace.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eventersapp.marketplace.data.model.*
import com.eventersapp.marketplace.data.repositories.ResellOrSendEventRepository
import com.eventersapp.marketplace.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ResellOrSendEventViewModel(private val repository: ResellOrSendEventRepository) :
    ViewModel() {

    companion object {
        const val ONLINE = "online"
        const val RESELL = "RESELL"
        const val ACTION_RESELL_TICKET = "action_resell_ticket"
        const val ACTION_SEND_TICKET = "action_send_ticket"
    }

    var eventTitle: String? = null
    var eventDescription: String? = null
    var ticketPrice: String? = null

    private var firebaseUserToken = ""
    private var deviceId = ""

    private var eventTicketId = -1
    private var priceToResell = -1
    private var publicEventId = -1
    private var toUserId = -1

    private var connectResponse: ConnectResponse? = null
    private var resellEventPostBody: ResellTicketEventPostBody = ResellTicketEventPostBody()
    private var sendEventPostBody: SendTicketEventPostBody = SendTicketEventPostBody()

    private val _messageLiveData = MutableLiveData<Event<String>>()
    val messageLiveData: LiveData<Event<String>>
        get() = _messageLiveData
    private val _resellEventLiveData =
        MutableLiveData<Event<State<ResellTicketEventResponse>>>()
    val resellEventLiveData: LiveData<Event<State<ResellTicketEventResponse>>>
        get() = _resellEventLiveData

    private val _sendEventLiveData =
        MutableLiveData<Event<State<SendTicketEventResponse>>>()
    val sendEventLiveData: LiveData<Event<State<SendTicketEventResponse>>>
        get() = _sendEventLiveData

    private lateinit var resellEventResponse: ResellTicketEventResponse
    private lateinit var sendEventResponse: SendTicketEventResponse


    private fun resellEvent() {
        resellEventPostBody = resellEventPostBodyJson()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                resellEventResponse = repository.resellEvent(resellEventPostBody)
                withContext(Dispatchers.Main) {
                    _resellEventLiveData.postValue(Event(State.success(resellEventResponse)))
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    _resellEventLiveData.postValue(Event(State.error(e.message!!)))
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _resellEventLiveData.postValue(Event(State.error(e.message!!)))
                }
            }
        }
    }

    private fun sendEvent() {
        sendEventPostBody = sendEventPostBodyJson()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                sendEventResponse = repository.sendEvent(sendEventPostBody)
                withContext(Dispatchers.Main) {
                    _sendEventLiveData.postValue(Event(State.success(sendEventResponse)))
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    _sendEventLiveData.postValue(Event(State.error(e.message!!)))
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _sendEventLiveData.postValue(Event(State.error(e.message!!)))
                }
            }
        }
    }

    fun setUserInfo(
        myEvent: MyEventListResponse.Data,
        userData: ConnectResponse?,
        id: String
    ) {
        connectResponse = userData
        deviceId = id
        eventTicketId = myEvent.eventTicket.eventTicketId
        eventTitle = myEvent.publicEvent.eventTitle
        eventDescription = myEvent.publicEvent.eventDescription
        ticketPrice = myEvent.eventTicket.price.toString()
        publicEventId = myEvent.publicEvent.publicEventId
    }

    private fun resellEventPostBodyJson(): ResellTicketEventPostBody {
        val authen = ResellTicketEventPostBody().Auth()
        authen.let {
            it.deviceId = deviceId
            it.tokenId = firebaseUserToken
            it.authType = ONLINE
            it.userId = connectResponse?.data?.user?.userId!!.toInt()
            it.pushKey = ""
        }
        val ticket = ResellTicketEventPostBody().Ticket()
        ticket.let {
            it.eventTicketId = eventTicketId
            it.status = RESELL
            it.priceToResell = priceToResell
        }
        val data = ResellTicketEventPostBody().Data()
        data.auth = authen
        data.ticket = ticket
        resellEventPostBody.data = data
        return resellEventPostBody
    }

    private fun sendEventPostBodyJson(): SendTicketEventPostBody {
        val authen = SendTicketEventPostBody().Auth()
        authen.let {
            it.deviceId = deviceId
            it.tokenId = firebaseUserToken
            it.authType = ONLINE
            it.userId = connectResponse?.data?.user?.userId!!.toInt()
            it.pushKey = ""
        }
        val ticket = SendTicketEventPostBody().Ticket()
        ticket.let {
            it.eventTicketId = eventTicketId
            it.publicEventId = publicEventId
            it.toUserId = toUserId
            it.fromUserId = connectResponse?.data?.user?.userId!!.toInt()

        }
        val data = SendTicketEventPostBody().Data()
        data.auth = authen
        data.ticket = ticket
        sendEventPostBody.data = data
        return sendEventPostBody
    }

    private fun getJWTToken(action: String) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseUserToken = getFirebaseUserToken()
            withContext(Dispatchers.Main) {
                if (action == ACTION_RESELL_TICKET)
                    resellEvent()
                else if (action == ACTION_SEND_TICKET)
                    sendEvent()
            }
        }
    }

    fun resellAmount(price: Int) {
        _resellEventLiveData.postValue(Event(State.loading()))
        priceToResell = price
        getJWTToken(ACTION_RESELL_TICKET)
    }

    fun sendToUser(id: Int) {
        _resellEventLiveData.postValue(Event(State.loading()))
        toUserId = id
        getJWTToken(ACTION_SEND_TICKET)
    }
}