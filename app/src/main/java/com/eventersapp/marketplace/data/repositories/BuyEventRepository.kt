package com.eventersapp.marketplace.data.repositories

import com.eventersapp.marketplace.data.model.BuyNormalTicketEventPostBody
import com.eventersapp.marketplace.data.model.BuyResellTicketEventPostBody
import com.eventersapp.marketplace.data.model.BuyTicketEventResponse
import com.eventersapp.marketplace.data.network.ApiInterface
import com.eventersapp.marketplace.data.network.SafeApiRequest

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