package io.github.alxiw.hello.core;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Sticker;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendSticker;
import com.pengrad.telegrambot.response.SendResponse;
import io.github.alxiw.hello.sys.AppLogger;

import java.io.IOException;

public class AppMessagesListener {

    private final TelegramBot bot;

    public AppMessagesListener(TelegramBot bot) {
        this.bot = bot;
    }

    public void onNewMessage(Message message) {
        long messageId = message.messageId();
        long chatId = message.chat().id();
        AppLogger.i("new message, chat: " + message.chat().id());
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
        bot.execute(new SendSticker(chatId, sticker.fileId()), new Callback<SendSticker, SendResponse>() {
            @Override
            public void onResponse(SendSticker sendSticker, SendResponse sendResponse) {
                AppLogger.i("response to message with id " + messageId + " is " + sendResponse.toString());
            }

            @Override
            public void onFailure(SendSticker sendSticker, IOException e) {
                AppLogger.e(e, "error occurred while sending response to message with id " + messageId);
            }
        });

    }

    private void onTextMessage(String text, long messageId, long chatId) {
        // if (content.startsWith("/start")) {
        //     AppLogger.i("received message is command: " + content);
        //     return;
        // }

        SendResponse response = bot.execute(new SendMessage(chatId, text.trim()));
        AppLogger.i("response to message with id " + messageId + " is " + response.toString());
    }
}
