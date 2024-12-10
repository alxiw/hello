package io.github.alxiw.hello.service.sticker;

import io.github.alxiw.hello.data.StickerDaoImpl;
import io.github.alxiw.hello.model.Sticker;
import io.github.alxiw.hello.service.Response;

import java.util.List;
import java.util.Random;

public class StickerService {

    private static volatile StickerService instance;

    public static StickerService getInstance() {
        // Первый проверка (не блокирующая)
        if (instance == null) {
            synchronized (StickerService.class) {
                // Второй проверка (блокирующая)
                if (instance == null) {
                    instance = new StickerService();
                }
            }
        }
        return instance;
    }

    private final StickerDao stickerDao;

    private StickerService() {
        this.stickerDao = new StickerDaoImpl();
    }

    public Response addSticker(String fileId, String emoji) {
        return getResponse(stickerDao.addSticker(fileId, emoji));
    }

    public Sticker getStickerById(int id) {
        return stickerDao.getStickerById(id);
    }

    public List<Sticker> getAllStickers() {
        return stickerDao.getAllStickers();
    }

    public Response updateSticker(Sticker account) {
        return getResponse(stickerDao.updateSticker(account));
    }

    public Response deleteSticker(int id) {
        return getResponse(stickerDao.deleteSticker(id));
    }

    public String getRandomSticker(String fileId) {
        List<Sticker> stickers = stickerDao.getAllStickers();
        if (stickers.isEmpty()) {
            return fileId;
        }

        Random random = new Random();
        int min = 1;
        int max = stickers.size();
        int randomNumber = random.nextInt(max - min) + min;
        Sticker sticker = stickers.get(randomNumber - 1);

        return sticker.getFileId();
    }

    private Response getResponse(int value) {
        if (value <= -1) {
            return Response.ERROR;
        } else if (value == 0) {
            return Response.NO_CHANGES;
        } else {
            return Response.SUCCESS;
        }
    }
}
