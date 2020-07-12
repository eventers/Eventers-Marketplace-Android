package com.eventersapp.consumer.data.repositories

import com.eventersapp.consumer.data.model.ResellTicketEventPostBody
import com.eventersapp.consumer.data.model.ResellTicketEventResponse
import com.eventersapp.consumer.data.model.SendTicketEventPostBody
import com.eventersapp.consumer.data.model.SendTicketEventResponse
import com.eventersapp.consumer.data.network.ApiInterface
import com.eventersapp.consumer.data.network.SafeApiRequest

class ResellOrSendEventRepository(
    private val api: ApiInterface
) : SafeApiRequest() {

    suspend fun resellEvent(
        resellTicketEventPostBody: ResellTicketEventPostBody
    ): ResellTicketEventResponse {

        return apiRequest { api.resellTicketData(resellTicketEventPostBody) }
    }

    suspend fun sendEvent(
        sendTicketEventPostBody: SendTicketEventPostBody
    ): SendTicketEventResponse {

        return apiRequest { api.sendTicketData(sendTicketEventPostBody) }
    }

}