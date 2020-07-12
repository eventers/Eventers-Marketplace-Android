package com.eventersapp.consumer.data.model

import com.google.gson.annotations.SerializedName

class SendTicketEventPostBody {
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

        @SerializedName("public_event_id")
        var publicEventId = 0

        @SerializedName("to_user_id")
        var toUserId = 0

        @SerializedName("from_user_id")
        var fromUserId = 0
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