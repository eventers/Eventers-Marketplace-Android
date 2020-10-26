package com.eventersapp.marketplace.data.local.dao

import androidx.room.*
import com.eventersapp.marketplace.data.model.Account

@Dao
interface AccountsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Account)

    @Query("DELETE FROM ${Account.TABLE_NAME}")
    suspend fun deleteAllTableRecords()

    @Query("SELECT * FROM ${Account.TABLE_NAME} WHERE ID = :accountId")
    suspend fun fetchAccountById(accountId: Int): Account

    @Query("SELECT * FROM ${Account.TABLE_NAME} WHERE accountAddress = :accountAddress")
    suspend fun fetchAccountByAddress(accountAddress: String): Account?

    @Query("SELECT * FROM ${Account.TABLE_NAME} WHERE isSelected = 1")
    suspend fun fetchAccountBySelectedValue(): Account

    @Query("SELECT * FROM ${Account.TABLE_NAME} WHERE passphrase = :passphrase")
    suspend fun fetchAccountByPassphrase(passphrase: String): Account?

    @Query("SELECT * FROM ${Account.TABLE_NAME} ORDER BY ID")
    suspend fun fetchAllAccount(): List<Account>

    @Query("UPDATE ${Account.TABLE_NAME} SET isSelected = 0")
    suspend fun updateIsSelectedColumn()

    @Update
    suspend fun updateAccount(account: Account): Int

    @Delete
    suspend fun deleteAccount(account: Account): Int


}