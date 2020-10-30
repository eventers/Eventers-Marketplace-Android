package com.eventersapp.marketplace.data.repositories

import com.eventersapp.marketplace.data.model.AllEventListResponse
import com.eventersapp.marketplace.data.network.ApiInterface
import com.eventersapp.marketplace.data.network.SafeApiRequest

class AllEventRepository(
    private val api: ApiInterface
) : SafeApiRequest() {

    suspend fun allEvent(): AllEventListResponse {

        return apiRequest { api.getAllEventData() }
    }


}