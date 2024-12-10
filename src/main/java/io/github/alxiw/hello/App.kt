package io.github.alxiw.hello

import com.pengrad.telegrambot.TelegramBot
import io.github.alxiw.hello.core.Router
import io.github.alxiw.hello.sys.AppLogger
import java.lang.RuntimeException

fun main() {
    System.getenv("BOT_TOKEN")?.also { token ->
        val bot = TelegramBot(token)
        AppLogger.i("successfully started")
        bot.setUpdatesListener(Router(bot)) { e ->
            e.response()?.also {
                AppLogger.e(e, "got bad response from telegram: ${it.errorCode()}, ${it.description()}")
            } ?: run {
                AppLogger.e(RuntimeException("probably network error"))
            }
        }
    } ?: run {
        AppLogger.e(RuntimeException("token is null"))
    }
}
