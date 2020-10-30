package com.eventersapp.marketplace.data.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class MyEventListResponse(
    @SerializedName("data")
    val `data`: ArrayList<Data>
) : Serializable {
    @Keep
    data class Data(
        @SerializedName("event_ticket")
        val eventTicket: EventTicket,
        @SerializedName("public_event")
        val publicEvent: PublicEvent
    ) : Serializable {
        @Keep
        data class EventTicket(
            @SerializedName("event_ticket_id")
            val eventTicketId: Int,
            @SerializedName("price")
            val price: Int,
            @SerializedName("public_event_id")
            val publicEventId: Int,
            @SerializedName("status")
            val status: String
        ) : Serializable

        @Keep
        data class PublicEvent(
            @SerializedName("date_time")
            val dateTime: String,
            @SerializedName("event_description")
            val eventDescription: String,
            @SerializedName("event_image")
            val eventImage: String,
            @SerializedName("event_title")
            val eventTitle: String,
            @SerializedName("public_event_id")
            val publicEventId: Int
        ) : Serializable
    }
}