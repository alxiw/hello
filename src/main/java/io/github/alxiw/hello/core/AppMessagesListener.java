package io.github.alxiw.hello.core;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Sticker;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendSticker;
import com.pengrad.telegrambot.response.SendResponse;
import io.github.alxiw.hello.data.AccountService;
import io.github.alxiw.hello.data.Response;
import io.github.alxiw.hello.data.StickerService;
import io.github.alxiw.hello.model.Joke;
import io.github.alxiw.hello.data.JokeService;
import io.github.alxiw.hello.sys.AppLogger;

import java.io.IOException;
import java.util.List;

public class AppMessagesListener {

    private final TelegramBot bot;

    private final JokeService jokeService;
    private final AccountService accountService;
    private final StickerService stickerService;

    public AppMessagesListener(TelegramBot bot) {
        this.bot = bot;

        this.jokeService = JokeService.getInstance();
        this.accountService = AccountService.getInstance();
        this.stickerService = StickerService.getInstance();
    }

    private String gatName(String fn, String ln) {
        String first = fn.trim();
        String last = ln.trim();
        if (first.isEmpty() && last.isEmpty()) {
            return "";
        }
        if (last.isEmpty()) {
            return first;
        }
        if (first.isEmpty()) {
            return last;
        }

        return first + " " + last;
    }

    public void onNewMessage(Message message) {
        long messageId = message.messageId();
        long chatId = message.chat().id();
        String name = gatName(message.chat().firstName(), message.chat().lastName());
        AppLogger.i("new message, chat: " + message.chat().id());
        if (accountService.getAllAccounts().stream().noneMatch(item -> item.getUin().equals(String.valueOf(chatId)))
                && accountService.addAccount(String.valueOf(chatId), name) == Response.SUCCESS) {
            AppLogger.i("account with id " + chatId + " added to database");
        }
        String text = message.text();
        if (message.sticker() != null) {
            Sticker sticker = message.sticker();
            AppLogger.i("received message " + messageId + " has sticker: " + sticker.fileId());
            onSticker(sticker, messageId, chatId);
            return;
        }

        if (text != null && !text.isEmpty()) {
            AppLogger.i("received message " + messageId + " has text: " + text);
            onTextMessage(text, messageId, chatId);
            return;
        }

        AppLogger.i("received message has empty content");
    }

    public void onEditedMessage(Message message) {
        AppLogger.i("EditedMessageEvent, chat:" + message.chat().id());
    }

    private void onSticker(Sticker sticker, long messageId, long chatId) {
        String fileId = sticker.fileId();
        if (stickerService.getAllStickers().stream().noneMatch(item -> item.getFileId().equals(fileId))
                && stickerService.addSticker(fileId, sticker.emoji()) == Response.SUCCESS) {
            AppLogger.i("sticker with id " + fileId + " added to database");
        }

        String id = stickerService.getRandomSticker(fileId);
        SendResponse response = bot.execute(new SendSticker(chatId, id));
        AppLogger.i("reply to message with id " + messageId + " is sticker with id " + id + ", response is ok – " + response.isOk());
    }

    private void onTextMessage(String text, long messageId, long chatId) {
        Joke joke =  jokeService.getRandomJoke();
        int id = joke.getId();
        String original = joke.getOriginal();
        String russian = joke.getRussian();
        String interpretation = joke.getInterpretation();

        String reply = "*№" + id + "*\n\n" + "`\n" + original + "\n`" + "\n" + "`\n" + russian + "\n`" + "\n\n" + interpretation;
        SendMessage message = new SendMessage(chatId, reply);
        message.parseMode(ParseMode.Markdown);
        SendResponse response = bot.execute(message);
        AppLogger.i("reply to message with id " + messageId + " is joke with id " + id + ", response is ok – " + response.isOk());
    }
}
