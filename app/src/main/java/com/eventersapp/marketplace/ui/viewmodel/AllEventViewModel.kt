package com.eventersapp.marketplace.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eventersapp.marketplace.data.model.AllEventListResponse
import com.eventersapp.marketplace.data.repositories.AllEventRepository
import com.eventersapp.marketplace.util.ApiException
import com.eventersapp.marketplace.util.Event
import com.eventersapp.marketplace.util.NoInternetException
import com.eventersapp.marketplace.util.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllEventViewModel(private val repository: AllEventRepository) :
    ViewModel() {

    private var allEventList = ArrayList<AllEventListResponse.Data?>()
    var isAllEventsApiCalled = false

    private val _allEventListLiveData =
        MutableLiveData<Event<State<ArrayList<AllEventListResponse.Data?>>>>()
    val allEventListLiveData: LiveData<Event<State<ArrayList<AllEventListResponse.Data?>>>>
        get() = _allEventListLiveData

    private lateinit var allEventListResponse: AllEventListResponse


    fun getAllEventList() {
        _allEventListLiveData.postValue(Event(State.loading()))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                allEventListResponse = repository.allEvent()
                withContext(Dispatchers.Main) {
                    if (allEventListResponse.data != null) {
                        allEventList.addAll(allEventListResponse.data)
                    }
                    isAllEventsApiCalled = true
                    _allEventListLiveData.postValue(Event(State.success(allEventList)))
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    _allEventListLiveData.postValue(Event(State.error(e.message ?: "")))
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _allEventListLiveData.postValue(Event(State.error(e.message ?: "")))
                }
            }
        }
    }

    fun getLoadedAllEventsList() {
        _allEventListLiveData.postValue(
            Event(
                State.success(
                    allEventList
                )
            )
        )
    }

    fun refreshAllEventListData() {
        getAllEventList()
    }
}