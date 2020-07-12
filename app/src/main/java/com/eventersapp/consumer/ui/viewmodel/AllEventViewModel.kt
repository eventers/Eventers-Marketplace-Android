package com.eventersapp.consumer.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eventersapp.consumer.data.model.AllEventListResponse
import com.eventersapp.consumer.data.repositories.AllEventRepository
import com.eventersapp.consumer.util.ApiException
import com.eventersapp.consumer.util.Event
import com.eventersapp.consumer.util.NoInternetException
import com.eventersapp.consumer.util.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllEventViewModel(private val repository: AllEventRepository) :
    ViewModel() {

    private var allEventList = ArrayList<AllEventListResponse.Data?>()

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
                    if (allEventListResponse.data != null)
                        allEventList.addAll(allEventListResponse.data)
                    _allEventListLiveData.postValue(Event(State.success(allEventList)))
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    _allEventListLiveData.postValue(Event(State.error(e.message!!)))
                }
            } catch (e: NoInternetException) {
                withContext(Dispatchers.Main) {
                    _allEventListLiveData.postValue(Event(State.error(e.message!!)))
                }
            }
        }
    }

}