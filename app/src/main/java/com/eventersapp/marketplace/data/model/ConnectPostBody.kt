package com.eventersapp.marketplace.data.model

import com.google.gson.annotations.SerializedName

class ConnectPostBody {
    @SerializedName("data")
    var data: Data? = null

    inner class Data {
        @SerializedName("auth")
        var auth: Auth? = null

        @SerializedName("user")
        var user: User? = null
    }

    inner class Auth {
        @SerializedName("device_provider")
        var deviceProvider: String? = null

        @SerializedName("device_id")
        var deviceId: String? = null

        @SerializedName("token_id")
        var tokenId: String? = null

        @SerializedName("fcm_token")
        var fcmToken: String? = null
    }

    inner class User {
        @SerializedName("phone_country_code")
        var phoneCountryCode: String? = null

        @SerializedName("provider")
        var provider: String? = null

        @SerializedName("phone_number")
        var phoneNumber: String? = null

        @SerializedName("phone_firebase_id")
        var phoneFirebaseId: String? = null

        @SerializedName("g_firebase_id")
        var gFirebaseId: String? = null

        @SerializedName("g_email")
        var gEmail: String? = null

        @SerializedName("g_name")
        var gName: String? = null

        @SerializedName("g_image_url")
        var gImageUrl: String? = null

        @SerializedName("fb_firebase_id")
        var fbFirebaseId: String? = null

        @SerializedName("fb_email")
        var fbEmail: String? = null

        @SerializedName("fb_name")
        var fbName: String? = null

        @SerializedName("fb_image_url")
        var fbImageUrl: String? = null

    }
}