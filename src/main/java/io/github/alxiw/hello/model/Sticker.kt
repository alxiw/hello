package io.github.alxiw.hello.model;

public class Sticker {

    private int id;
    private String fileId;
    private String emoji;

    public Sticker(int id, String fileId, String emoji) {
        this.id = id;
        this.fileId = fileId;
        this.emoji = emoji;
    }

    public int getId() {
        return id;
    }

    public String getFileId() {
        return fileId;
    }

    public String getEmoji() {
        return emoji;
    }
}