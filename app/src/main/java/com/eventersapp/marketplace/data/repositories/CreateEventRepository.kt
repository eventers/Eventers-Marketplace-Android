package com.eventersapp.marketplace.data.repositories

import com.eventersapp.marketplace.data.model.CreateEventPostBody
import com.eventersapp.marketplace.data.model.CreateEventResponse
import com.eventersapp.marketplace.data.network.ApiInterface
import com.eventersapp.marketplace.data.network.SafeApiRequest

class CreateEventRepository(
    private val api: ApiInterface
) : SafeApiRequest() {

    suspend fun createEvent(
        createEventPostBody: CreateEventPostBody
    ): CreateEventResponse {

        return apiRequest { api.createEventData(createEventPostBody) }
    }

}