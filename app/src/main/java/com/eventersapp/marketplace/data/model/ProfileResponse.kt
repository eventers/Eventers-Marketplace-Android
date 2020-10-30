package com.eventersapp.marketplace.data.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ProfileResponse(
    @SerializedName("data")
    val `data`: Data
) {
    @Keep
    data class Data(
        @SerializedName("user")
        val user: User
    ) {
        @Keep
        data class User(
            @SerializedName("account_address")
            val accountAddress: String,
            @SerializedName("fb_firebase_id")
            val fbFirebaseId: String,
            @SerializedName("fb_image_url")
            val fbImageUrl: String,
            @SerializedName("fb_name")
            val fbName: String,
            @SerializedName("fb_email")
            val fbEmail: String,
            @SerializedName("g_email")
            val gEmail: String,
            @SerializedName("g_firebase_id")
            val gFirebaseId: String,
            @SerializedName("g_image_url")
            val gImageUrl: String,
            @SerializedName("g_name")
            val gName: String,
            @SerializedName("phone_country_code")
            val phoneCountryCode: String,
            @SerializedName("phone_number")
            val phoneNumber: String,
            @SerializedName("provider")
            val provider: String,
            @SerializedName("user_id")
            val userId: Int
        )
    }
}