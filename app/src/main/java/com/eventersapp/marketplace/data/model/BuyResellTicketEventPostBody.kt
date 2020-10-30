package com.eventersapp.marketplace.data.model

import com.google.gson.annotations.SerializedName

class BuyResellTicketEventPostBody {
    @SerializedName("data")
    var data: Data? = null

    inner class Data {
        @SerializedName("ticket")
        var ticket: Ticket? = null

        @SerializedName("auth")
        var auth: Auth? = null

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

    inner class Ticket {
        @SerializedName("public_event_id")
        var publicEventId = 0

        @SerializedName("event_ticket_id")
        var eventTicketId = 0

        @SerializedName("to_user_id")
        var toUserId = 0

    }

}