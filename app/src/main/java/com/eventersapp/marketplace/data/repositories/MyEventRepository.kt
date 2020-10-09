package com.eventersapp.marketplace.data.repositories

import com.eventersapp.marketplace.data.model.MyEventListResponse
import com.eventersapp.marketplace.data.network.ApiInterface
import com.eventersapp.marketplace.data.network.SafeApiRequest

class MyEventRepository(
    private val api: ApiInterface
) : SafeApiRequest() {

    suspend fun myEvent(
        userId: Int
    ): MyEventListResponse {

        return apiRequest { api.getMyEventData(userId) }
    }


}