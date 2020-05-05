package io.github.alxiw.icq.geekbot.disp;

public class Message {

    private final long id;
    private final String chatId;

    public Message(long id, String chatId) {
        this.id = id;
        this.chatId = chatId;
    }

    public long getId() {
        return id;
    }

    public String getChatId() {
        return chatId;
    }
}
