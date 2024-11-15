package io.github.alxiw.hello.data;

import io.github.alxiw.hello.model.Sticker;

import java.util.List;

interface StickerDao {

    int addSticker (String fileId, String emoji);

    Sticker getStickerById(int id);

    List<Sticker> getAllStickers();

    int updateSticker(Sticker sticker);

    int deleteSticker(int id);
}
