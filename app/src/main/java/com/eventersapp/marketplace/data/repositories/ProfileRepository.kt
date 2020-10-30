package com.eventersapp.marketplace.data.repositories

import com.eventersapp.marketplace.data.local.AppDatabase
import com.eventersapp.marketplace.data.model.Account
import com.eventersapp.marketplace.data.model.LogoutPostBody
import com.eventersapp.marketplace.data.model.LogoutResponse
import com.eventersapp.marketplace.data.model.ProfileResponse
import com.eventersapp.marketplace.data.network.ApiInterface
import com.eventersapp.marketplace.data.network.SafeApiRequest

class ProfileRepository(
    private val api: ApiInterface,
    private val db: AppDatabase
) : SafeApiRequest() {

    suspend fun logout(
        logoutPostBody: LogoutPostBody
    ): LogoutResponse {

        return apiRequest { api.logoutData(logoutPostBody) }
    }

    suspend fun getProfile(
        id: Int, tokenId: String
    ): ProfileResponse {
        val profileResponse = apiRequest { api.getProfileData(id, tokenId) }
        saveAccountDetail(Account("Default", profileResponse.data.user.accountAddress, "", true))
        return profileResponse
    }

    private suspend fun saveAccountDetail(account: Account) {
        val addr = account.accountAddress?.let { address ->
            db.getAccountsDao().fetchAccountByAddress(address)
        }
        if (addr == null)
            db.getAccountsDao().insertAccount(account)
    }

    suspend fun fetchSelectedAddressFromDb(): Account {
        return db.getAccountsDao().fetchAccountBySelectedValue()
    }

    suspend fun deleteAllTableRecordsFromDb() {
        db.getAccountsDao().deleteAllTableRecords()
    }

}