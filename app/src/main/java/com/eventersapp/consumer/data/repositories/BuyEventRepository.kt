package com.eventersapp.consumer.data.repositories

import com.eventersapp.consumer.data.model.BuyNormalTicketEventPostBody
import com.eventersapp.consumer.data.model.BuyResellTicketEventPostBody
import com.eventersapp.consumer.data.model.BuyTicketEventResponse
import com.eventersapp.consumer.data.network.ApiInterface
import com.eventersapp.consumer.data.network.SafeApiRequest

class BuyEventRepository(
    private val api: ApiInterface
) : SafeApiRequest() {

    suspend fun buyNormalTicket(
        buyNormalTicketEventPostBody: BuyNormalTicketEventPostBody
    ): BuyTicketEventResponse {

        return apiRequest { api.buyNormalTicketData(buyNormalTicketEventPostBody) }
    }

    suspend fun buyResellTicket(
        buyResellTicketEventPostBody: BuyResellTicketEventPostBody
    ): BuyTicketEventResponse {

        return apiRequest { api.buyResellTicketData(buyResellTicketEventPostBody) }
    }

}