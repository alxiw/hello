package io.github.alxiw.hello.service.sticker

import io.github.alxiw.hello.data.model.Sticker

interface IStickerDao {
    fun addSticker(fileId: String, emoji: String): Int
    fun getStickerById(id: Int): Sticker?
    fun getAllStickers(): List<Sticker>
    fun updateSticker(sticker: Sticker): Int
    fun deleteSticker(id: Int): Int
}
