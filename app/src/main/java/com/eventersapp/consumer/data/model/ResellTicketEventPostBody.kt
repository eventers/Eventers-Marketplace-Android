package com.eventersapp.consumer.data.model

import com.google.gson.annotations.SerializedName

class ResellTicketEventPostBody {
    @SerializedName("data")
    var data: Data? = null

    inner class Data {
        @SerializedName("ticket")
        var ticket: Ticket? = null

        @SerializedName("auth")
        var auth: Auth? = null
    }

    inner class Ticket {
        @SerializedName("event_ticket_id")
        var eventTicketId = 0

        @SerializedName("status")
        var status: String? = null

        @SerializedName("price_to_resell")
        var priceToResell = 0
    }

    inner class Auth {
        @SerializedName("auth_type")
        var authType: String? = null

        @SerializedName("device_id")
        var deviceId: String? = null

        @SerializedName("token_id")
        var tokenId: String? = null

        @SerializedName("user_id")
        var userId = 0

        @SerializedName("push_key")
        var pushKey: String? = null
    }
}