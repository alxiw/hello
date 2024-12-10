package io.github.alxiw.hello;

import com.pengrad.telegrambot.TelegramBot;
import io.github.alxiw.hello.core.AppUpdatesListener;
import io.github.alxiw.hello.sys.AppLogger;

public class App {

    public static void main(String[] args) {
        String token = System.getenv("BOT_TOKEN");
        if (token == null) {
            AppLogger.e(new RuntimeException("token is null"));
            return;
        }
        TelegramBot bot = new TelegramBot(token);
        AppLogger.i("successfully started");
        bot.setUpdatesListener(new AppUpdatesListener(bot), e -> {
            if (e.response() != null) {
                // got bad response from telegram
                AppLogger.e(e, "error occurred during setting updates listener: " + e.response().errorCode() + " " + e.response().description());
            } else {
                // probably network error
                AppLogger.e(new RuntimeException("network error"), e.getMessage());
            }
        });
    }
}
