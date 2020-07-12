package com.eventersapp.consumer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eventersapp.consumer.data.model.*
import com.eventersapp.consumer.data.repositories.BuyEventRepository
import com.eventersapp.consumer.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class BuyEventViewModel(private val repository: BuyEventRepository) : ViewModel() {


    private var firebaseUserToken = ""
    private var deviceId = ""
    private var toUserId = -1
    private var publicEventId = -1

    private var connectResponse: ConnectResponse? = null
    private var buyNormalTicketEventPostBody: BuyNormalTicketEventPostBody =
        BuyNormalTicketEventPostBody()
    private var buyResellTicketEventPostBody: BuyResellTicketEventPostBody =
        BuyResellTicketEventPostBody()

    private val _messageLiveData = MutableLiveData<Event<String>>()
    val messageLiveData: LiveData<Event<String>>
        get() = _messageLiveData

    private val _buyNormalTicketEventLiveData =
        MutableLiveData<Event<State<BuyTicketEventResponse>>>()
    val buyNormalTicketEventLiveData: LiveData<Event<State<BuyTicketEventResponse>>>
        get() = _buyNormalTicketEventLiveData
    private lateinit var buyNormalTicketEventResponse: BuyTicketEventResponse

    private val _buyResellTicketEventLiveData =
        MutableLiveData<Event<State<BuyTicketEventResponse>>>()
    val buyResellTicketEventLiveData: LiveData<Event<State<BuyTicketEventResponse>>>
        get() = _buyResellTicketEventLiveData
    private lateinit var buyResellTicketEventResponse: BuyTicketEventResponse

    fun buyNormalTicket() {
        _buyNormalTicketEventLiveData.postValue(Event(State.loading()))
        buyNormalTicketEventPostBody = buyNormalTicketEventPostBodyJson()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                buyNormalTicketEventResponse =
                    repository.buyNormalTicket(buyNormalTicketEventPostBody)
                withContext(Dispatchers.Main) {
                    _buyNormalTicketEventLiveData.postValue(
                        Event(
                            State.success(
                                buyNormalTicketEventResponse
                            )
                        )
                    )
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    _buyNormalTicketEventLiveData.postValue(Event(State.error(e.message!!)))
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _buyNormalTicketEventLiveData.postValue(Event(State.error(e.message!!)))
                }
            }
        }
    }

    fun buyResellTicket(eventTicketId: Int) {
        _buyResellTicketEventLiveData.postValue(Event(State.loading()))
        buyResellTicketEventPostBody = buyResellTicketEventPostBodyJson(eventTicketId)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                buyResellTicketEventResponse =
                    repository.buyResellTicket(buyResellTicketEventPostBody)
                withContext(Dispatchers.Main) {
                    _buyResellTicketEventLiveData.postValue(
                        Event(
                            State.success(
                                buyResellTicketEventResponse
                            )
                        )
                    )
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    _buyResellTicketEventLiveData.postValue(Event(State.error(e.message!!)))
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _buyResellTicketEventLiveData.postValue(Event(State.error(e.message!!)))
                }
            }
        }
    }

    private fun buyNormalTicketEventPostBodyJson(): BuyNormalTicketEventPostBody {
        val authen = BuyNormalTicketEventPostBody().Auth()
        authen.let {
            it.deviceId = deviceId
            it.tokenId = firebaseUserToken
            it.authType = ResellOrSendEventViewModel.ONLINE
            it.userId = connectResponse?.data?.user?.userId!!.toInt()
            it.pushKey = ""
        }
        val ticket = BuyNormalTicketEventPostBody().Ticket()
        ticket.let {
            it.publicEventId = publicEventId
            it.toUserId = toUserId
        }
        val data = BuyNormalTicketEventPostBody().Data()
        data.auth = authen
        data.ticket = ticket
        buyNormalTicketEventPostBody.data = data
        return buyNormalTicketEventPostBody
    }

    private fun buyResellTicketEventPostBodyJson(eventTicketId: Int): BuyResellTicketEventPostBody {
        val authen = BuyResellTicketEventPostBody().Auth()
        authen.let {
            it.deviceId = deviceId
            it.tokenId = firebaseUserToken
            it.authType = ResellOrSendEventViewModel.ONLINE
            it.userId = toUserId
            it.pushKey = ""
        }
        val ticket = BuyResellTicketEventPostBody().Ticket()
        ticket.let {
            it.publicEventId = publicEventId
            it.toUserId = toUserId
            it.eventTicketId = eventTicketId
        }
        val data = BuyResellTicketEventPostBody().Data()
        data.auth = authen
        data.ticket = ticket
        buyResellTicketEventPostBody.data = data
        return buyResellTicketEventPostBody
    }

    fun getUserInfo(
        allEvent: AllEventListResponse.Data,
        userData: ConnectResponse?,
        id: String
    ) {
        connectResponse = userData
        deviceId = id
        toUserId = connectResponse?.data?.user?.userId!!.toInt()
        publicEventId = allEvent.publicEvent.publicEventId
        viewModelScope.launch(Dispatchers.IO) {
            firebaseUserToken = getFirebaseUserToken()
        }
    }

}