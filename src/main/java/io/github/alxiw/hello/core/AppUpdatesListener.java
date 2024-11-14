package io.github.alxiw.hello.core;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import io.github.alxiw.hello.sys.AppLogger;

import java.util.List;

public class AppUpdatesListener implements UpdatesListener {

    private final AppMessagesListener listener;

    public AppUpdatesListener(TelegramBot bot) {
        this.listener = new AppMessagesListener(bot);
    }

    @Override
    public int process(List<Update> updates) {
        AppLogger.i("received updates size: " + updates.size());
        updates.forEach(update -> accept(update, listener));
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void accept(Update update, AppMessagesListener appMessagesListener) {
        if (update.message() != null) {
            appMessagesListener.onNewMessage(update.message());
        } else if (update.editedMessage() != null) {
            appMessagesListener.onEditedMessage(update.message());
        } else {
            AppLogger.i("unknown update: " + update);
        }
    }
}
