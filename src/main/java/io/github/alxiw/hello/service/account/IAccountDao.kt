package io.github.alxiw.hello.service.account

import io.github.alxiw.hello.data.model.Account

interface IAccountDao {
    fun addAccount(uin: String, name: String): Int
    fun getAccountById(id: Int): Account?
    fun getAllAccounts(): List<Account>
    fun updateAccount(account: Account): Int
    fun deleteAccount(id: Int): Int
}
