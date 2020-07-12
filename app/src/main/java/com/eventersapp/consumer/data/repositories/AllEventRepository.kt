package com.eventersapp.consumer.data.repositories

import com.eventersapp.consumer.data.model.AllEventListResponse
import com.eventersapp.consumer.data.network.ApiInterface
import com.eventersapp.consumer.data.network.SafeApiRequest

class AllEventRepository(
    private val api: ApiInterface
) : SafeApiRequest() {

    suspend fun allEvent(): AllEventListResponse {

        return apiRequest { api.getAllEventData() }
    }


}