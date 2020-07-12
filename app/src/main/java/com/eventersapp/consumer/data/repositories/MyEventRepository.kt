package com.eventersapp.consumer.data.repositories

import com.eventersapp.consumer.data.model.MyEventListResponse
import com.eventersapp.consumer.data.network.ApiInterface
import com.eventersapp.consumer.data.network.SafeApiRequest

class MyEventRepository(
    private val api: ApiInterface
) : SafeApiRequest() {

    suspend fun myEvent(
        userId: Int
    ): MyEventListResponse {

        return apiRequest { api.getMyEventData(userId) }
    }


}