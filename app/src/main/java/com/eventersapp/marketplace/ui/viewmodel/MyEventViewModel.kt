package com.eventersapp.marketplace.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eventersapp.marketplace.data.model.MyEventListResponse
import com.eventersapp.marketplace.data.repositories.MyEventRepository
import com.eventersapp.marketplace.util.ApiException
import com.eventersapp.marketplace.util.Event
import com.eventersapp.marketplace.util.NoInternetException
import com.eventersapp.marketplace.util.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyEventViewModel(private val repository: MyEventRepository) : ViewModel() {

    private var myEventList = ArrayList<MyEventListResponse.Data?>()
    var isMyEventApiCalled = false

    private val _myEventListLiveData =
        MutableLiveData<Event<State<ArrayList<MyEventListResponse.Data?>>>>()
    val myEventListLiveData: LiveData<Event<State<ArrayList<MyEventListResponse.Data?>>>>
        get() = _myEventListLiveData

    private lateinit var myEventListResponse: MyEventListResponse


    fun getMyEventList(userId: Int) {
        _myEventListLiveData.postValue(Event(State.loading()))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                myEventListResponse = repository.myEvent(userId)
                withContext(Dispatchers.Main) {
                    if (myEventListResponse.data != null) {
                        myEventList.addAll(myEventListResponse.data)
                    }
                    isMyEventApiCalled = true
                    _myEventListLiveData.postValue(Event(State.success(myEventList)))
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    _myEventListLiveData.postValue(Event(State.error(e.message!!)))
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _myEventListLiveData.postValue(Event(State.error(e.message!!)))
                }
            }
        }
    }

    fun getLoadedMyEventsList() {
        _myEventListLiveData.postValue(
            Event(
                State.success(
                    myEventList
                )
            )
        )
    }

    fun refreshMyEventListData(userId: Int) {
        getMyEventList(userId)
    }
}