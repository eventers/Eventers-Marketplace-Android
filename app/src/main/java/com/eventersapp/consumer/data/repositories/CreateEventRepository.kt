package com.eventersapp.consumer.data.repositories

import com.eventersapp.consumer.data.model.CreateEventPostBody
import com.eventersapp.consumer.data.model.CreateEventResponse
import com.eventersapp.consumer.data.network.ApiInterface
import com.eventersapp.consumer.data.network.SafeApiRequest

class CreateEventRepository(
    private val api: ApiInterface
) : SafeApiRequest() {

    suspend fun createEvent(
        createEventPostBody: CreateEventPostBody
    ): CreateEventResponse {

        return apiRequest { api.createEventData(createEventPostBody) }
    }

}