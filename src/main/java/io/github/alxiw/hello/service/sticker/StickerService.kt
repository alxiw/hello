package io.github.alxiw.hello.service.sticker

import io.github.alxiw.hello.data.StickerDao
import io.github.alxiw.hello.data.model.Sticker
import io.github.alxiw.hello.sys.Response
import io.github.alxiw.hello.sys.Utils.getDatabaseResponse
import kotlin.concurrent.Volatile
import kotlin.random.Random

class StickerService private constructor() {

    private val stickerDao: IStickerDao = StickerDao()

    fun addSticker(fileId: String, emoji: String): Response {
        return getDatabaseResponse(stickerDao.addSticker(fileId, emoji))
    }

    fun getStickerById(id: Int): Sticker? {
        return stickerDao.getStickerById(id)
    }

    fun getAllStickers(): List<Sticker> {
        return stickerDao.getAllStickers()
    }

    fun updateSticker(account: Sticker): Response {
        return getDatabaseResponse(stickerDao.updateSticker(account))
    }

    fun deleteSticker(id: Int): Response {
        return getDatabaseResponse(stickerDao.deleteSticker(id))
    }

    fun getRandomSticker(fileId: String): String {
        val stickers = stickerDao.getAllStickers()
        if (stickers.isEmpty()) {
            return fileId
        }

        return stickers[Random.nextInt(0, stickers.size)].fileId
    }

    companion object {

        @Volatile
        private var instance: StickerService? = null

        @JvmStatic
        fun getInstance(): StickerService {
            return instance ?: StickerService().also { instance = it }
        }
    }
}
