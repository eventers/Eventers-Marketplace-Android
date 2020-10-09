package com.eventersapp.marketplace.data.repositories

import com.eventersapp.marketplace.data.local.AppDatabase
import com.eventersapp.marketplace.data.model.Account
import com.eventersapp.marketplace.data.model.LogoutPostBody
import com.eventersapp.marketplace.data.model.LogoutResponse
import com.eventersapp.marketplace.data.model.ProfileResponse
import com.eventersapp.marketplace.data.network.ApiInterface
import com.eventersapp.marketplace.data.network.SafeApiRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class AccountDetailsRepository(
    private val db: AppDatabase
) : SafeApiRequest() {

    suspend fun saveAccountDetail(account: Account) {
        db.getAccountsDao().insertAccount(account)
    }

}