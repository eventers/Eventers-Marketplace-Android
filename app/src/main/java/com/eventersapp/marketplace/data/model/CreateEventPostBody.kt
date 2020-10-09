package com.eventersapp.marketplace.data.model

import com.google.gson.annotations.SerializedName

class CreateEventPostBody {
    @SerializedName("data")
    var data: Data? = null

    inner class Data {
        @SerializedName("public_event")
        var publicEvent: PublicEvent? = null

        @SerializedName("auth")
        var auth: Auth? = null
    }

    inner class PublicEvent {
        @SerializedName("total_tickets")
        var totalTickets = 0

        @SerializedName("date_time")
        var dateTime: String? = null

        @SerializedName("ticket_price")
        var ticketPrice = 0

        @SerializedName("event_description")
        var eventDescription: String? = null

        @SerializedName("event_image")
        var eventImage: String? = null

        @SerializedName("event_title")
        var eventTitle: String? = null
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