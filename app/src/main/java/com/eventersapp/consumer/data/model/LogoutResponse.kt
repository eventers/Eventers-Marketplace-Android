package com.eventersapp.consumer.data.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class LogoutResponse(
    @SerializedName("data")
    val `data`: Data
) {
    @Keep
    data class Data(
        @SerializedName("user")
        val user: User,
        @SerializedName("auth")
        val auth: Auth
    ) {
        @Keep
        data class User(
            @SerializedName("user_id")
            val userId: Int
        )

        @Keep
        data class Auth(
            @SerializedName("status")
            val status: String
        )

    }
}

