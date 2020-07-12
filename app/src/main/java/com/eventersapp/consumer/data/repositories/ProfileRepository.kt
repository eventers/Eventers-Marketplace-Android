package com.eventersapp.consumer.data.repositories

import com.eventersapp.consumer.data.model.LogoutPostBody
import com.eventersapp.consumer.data.model.LogoutResponse
import com.eventersapp.consumer.data.model.ProfileResponse
import com.eventersapp.consumer.data.network.ApiInterface
import com.eventersapp.consumer.data.network.SafeApiRequest

class ProfileRepository(
    private val api: ApiInterface
) : SafeApiRequest() {

    suspend fun logout(
        logoutPostBody: LogoutPostBody
    ): LogoutResponse {

        return apiRequest { api.logoutData(logoutPostBody) }
    }

    suspend fun getProfile(
        id: Int, tokenId: String
    ): ProfileResponse {
        return apiRequest { api.getProfileData(id, tokenId) }
    }
}