package com.eventersapp.marketplace.data.repositories

import com.eventersapp.marketplace.data.local.AppDatabase
import com.eventersapp.marketplace.data.model.Account
import com.eventersapp.marketplace.data.network.SafeApiRequest

class RecoverPassphraseRepository(
    private val db: AppDatabase
) : SafeApiRequest() {

    suspend fun fetchAccountByPassphrase(passphrase: String): Account? {
        return db.getAccountsDao().fetchAccountByPassphrase(passphrase)
    }

    suspend fun saveAccountDetail(account: Account) {
        db.getAccountsDao().insertAccount(account)
    }

}