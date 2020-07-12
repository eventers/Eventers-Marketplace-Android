package com.eventersapp.consumer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eventersapp.consumer.data.model.MyEventListResponse
import com.eventersapp.consumer.data.repositories.MyEventRepository
import com.eventersapp.consumer.util.ApiException
import com.eventersapp.consumer.util.Event
import com.eventersapp.consumer.util.NoInternetException
import com.eventersapp.consumer.util.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyEventViewModel(private val repository: MyEventRepository) : ViewModel() {

    private var userId = -1
    private var myEventList = ArrayList<MyEventListResponse.Data?>()

    private val _myEventListLiveData =
        MutableLiveData<Event<State<ArrayList<MyEventListResponse.Data?>>>>()
    val myEventListLiveData: LiveData<Event<State<ArrayList<MyEventListResponse.Data?>>>>
        get() = _myEventListLiveData

    private lateinit var myEventListResponse: MyEventListResponse


    private fun getMyEventList() {
        _myEventListLiveData.postValue(Event(State.loading()))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                myEventListResponse = repository.myEvent(userId)
                withContext(Dispatchers.Main) {
                    if (myEventListResponse.data != null)
                        myEventList.addAll(myEventListResponse.data)
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

    fun getUserInfo(id: Int) {
        userId = id
        getMyEventList()
    }


}