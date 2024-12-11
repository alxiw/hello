package io.github.alxiw.hello.service.account

import io.github.alxiw.hello.data.AccountDao
import io.github.alxiw.hello.data.model.Account
import io.github.alxiw.hello.sys.Response
import io.github.alxiw.hello.sys.Utils.getDatabaseResponse
import kotlin.concurrent.Volatile

class AccountService private constructor() {

    private val accountDao: IAccountDao = AccountDao()

    fun addAccount(uin: String, name: String): Response {
        return getDatabaseResponse(accountDao.addAccount(uin, name))
    }

    fun getAccountById(id: Int): Account? {
        return accountDao.getAccountById(id)
    }

    fun getAllAccounts(): List<Account> {
        return accountDao.getAllAccounts()
    }

    fun updateAccount(account: Account): Response {
        return getDatabaseResponse(accountDao.updateAccount(account))
    }

    fun deleteAccount(id: Int): Response {
        return getDatabaseResponse(accountDao.deleteAccount(id))
    }

    companion object {

        @Volatile
        private var instance: AccountService? = null

        @JvmStatic
        fun getInstance(): AccountService {
            return instance ?: AccountService().also { instance = it }
        }
    }
}
