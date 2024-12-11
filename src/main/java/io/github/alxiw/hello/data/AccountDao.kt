package io.github.alxiw.hello.data

import io.github.alxiw.hello.sys.Const.DB_URL
import io.github.alxiw.hello.data.model.Account
import io.github.alxiw.hello.service.account.IAccountDao
import io.github.alxiw.hello.sys.AppLogger
import java.sql.DriverManager
import java.sql.SQLException
import java.util.ArrayList

class AccountDao : IAccountDao {

    override fun addAccount(uin: String, name: String): Int {
        val sql = "INSERT INTO accounts (uin, name) VALUES (?, ?)"
        try {
            DriverManager.getConnection(DB_URL).use { connection ->
                connection.prepareStatement(sql).use { ps ->
                    ps.setString(1, uin)
                    ps.setString(2, name)
                    return ps.executeUpdate()
                }
            }
        } catch (e: SQLException) {
            AppLogger.e(e)
        }

        return -1
    }

    override fun getAccountById(id: Int): Account? {
        val sql = "SELECT * FROM accounts WHERE id = ?"
        try {
            DriverManager.getConnection(DB_URL).use { connection ->
                connection.prepareStatement(sql).use { ps ->
                    ps.setInt(1, id)
                    val rs = ps.executeQuery()
                    if (rs.next()) {
                        return Account(
                            rs.getInt("id"),
                            rs.getString("uin"),
                            rs.getString("name")
                        )
                    }
                }
            }
        } catch (e: SQLException) {
            AppLogger.e(e)
        }

        AppLogger.i("an error occurred while getting account by id")
        return null
    }

    override fun getAllAccounts(): List<Account> {
        val users = ArrayList<Account>()
        val sql = "SELECT * FROM accounts"
        try {
            DriverManager.getConnection(DB_URL).use { connection ->
                connection.createStatement().use { cs ->
                    val rs = cs.executeQuery(sql)
                    while (rs.next()) {
                        val account = Account(
                            rs.getInt("id"),
                            rs.getString("uin"),
                            rs.getString("name")
                        )
                        users.add(account)
                    }
                }
            }
        } catch (e: SQLException) {
            AppLogger.e(e)
        }

        return users
    }

    override fun updateAccount(account: Account): Int {
        val sql = "UPDATE accounts SET uin = ?, name = ? WHERE id = ?"
        try {
            DriverManager.getConnection(DB_URL).use { connection ->
                connection.prepareStatement(sql).use { ps ->
                    ps.setString(1, account.uin)
                    ps.setString(2, account.name)
                    ps.setInt(3, account.id)
                    return ps.executeUpdate()
                }
            }
        } catch (e: SQLException) {
            AppLogger.e(e)
        }

        return -1
    }

    override fun deleteAccount(id: Int): Int {
        val sql = "DELETE FROM accounts WHERE id = ?"
        try {
            DriverManager.getConnection(DB_URL).use { connection ->
                connection.prepareStatement(sql).use { ps ->
                    ps.setInt(1, id)
                    return ps.executeUpdate()
                }
            }
        } catch (e: SQLException) {
            AppLogger.e(e)
        }

        return -1
    }
}
