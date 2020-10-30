package com.eventersapp.marketplace.data.repositories

import com.eventersapp.marketplace.data.model.ResellTicketEventPostBody
import com.eventersapp.marketplace.data.model.ResellTicketEventResponse
import com.eventersapp.marketplace.data.model.SendTicketEventPostBody
import com.eventersapp.marketplace.data.model.SendTicketEventResponse
import com.eventersapp.marketplace.data.network.ApiInterface
import com.eventersapp.marketplace.data.network.SafeApiRequest

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