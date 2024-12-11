package io.github.alxiw.hello.data

import io.github.alxiw.hello.sys.Const.DB_URL
import io.github.alxiw.hello.data.model.Sticker
import io.github.alxiw.hello.service.sticker.IStickerDao
import io.github.alxiw.hello.sys.AppLogger
import java.sql.DriverManager
import java.sql.SQLException
import java.util.ArrayList

class StickerDao : IStickerDao {

    override fun addSticker(fileId: String, emoji: String): Int {
        val sql = "INSERT INTO stickers (file_id, emoji) VALUES (?, ?)"
        try {
            DriverManager.getConnection(DB_URL).use { connection ->
                connection.prepareStatement(sql).use { ps ->
                    ps.setString(1, fileId)
                    ps.setString(2, emoji)
                    return ps.executeUpdate()
                }
            }
        } catch (e: SQLException) {
            AppLogger.e(e)
        }

        return -1
    }

    override fun getStickerById(id: Int): Sticker? {
        val sql = "SELECT * FROM stickers WHERE id = ?"
        try {
            DriverManager.getConnection(DB_URL).use { connection ->
                connection.prepareStatement(sql).use { ps ->
                    ps.setInt(1, id)
                    val rs = ps.executeQuery()
                    if (rs.next()) {
                        return Sticker(
                            rs.getInt("id"),
                            rs.getString("file_id"),
                            rs.getString("emoji")
                        )
                    }
                }
            }
        } catch (e: SQLException) {
            AppLogger.e(e)
        }
        return null
    }

    override fun getAllStickers(): List<Sticker> {
        val stickers = ArrayList<Sticker>()
        val sql = "SELECT * FROM stickers"
        try {
            DriverManager.getConnection(DB_URL).use { connection ->
                connection.createStatement().use { stmt ->
                    val rs = stmt.executeQuery(sql)
                    while (rs.next()) {
                        val sticker = Sticker(
                            rs.getInt("id"),
                            rs.getString("file_id"),
                            rs.getString("emoji")
                        )
                        stickers.add(sticker)
                    }
                }
            }
        } catch (e: SQLException) {
            AppLogger.e(e)
        }

        return stickers
    }

    override fun updateSticker(sticker: Sticker): Int {
        val sql = "UPDATE stickers SET file_id = ?, emoji = ? WHERE id = ?"
        try {
            DriverManager.getConnection(DB_URL).use { connection ->
                connection.prepareStatement(sql).use { ps ->
                    ps.setString(1, sticker.fileId)
                    ps.setString(2, sticker.emoji)
                    ps.setInt(3, sticker.id)
                    return ps.executeUpdate()
                }
            }
        } catch (e: SQLException) {
            AppLogger.e(e)
        }

        return -1
    }

    override fun deleteSticker(id: Int): Int {
        val sql = "DELETE FROM stickers WHERE id = ?"
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
