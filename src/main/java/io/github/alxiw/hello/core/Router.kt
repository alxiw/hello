package io.github.alxiw.hello.core

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.Update
import io.github.alxiw.hello.sys.AppLogger

class Router(bot: TelegramBot) : UpdatesListener {

    private val listener = MessagesListener(bot)

    override fun process(updates: MutableList<Update>): Int {
        AppLogger.i("updates received, size: " + updates.size)
        updates.forEach { update -> accept(update, listener) }

        return UpdatesListener.CONFIRMED_UPDATES_ALL
    }

    private fun accept(update: Update, messagesListener: MessagesListener) = when {
        exist(update.message()) -> messagesListener.onNewMessage(update.message())
        exist(update.editedMessage()) -> messagesListener.onEditedMessage(update.message())
        exist(update.messageReaction()) -> messagesListener.onMessageReaction(update.messageReaction())
        exist(update.messageReactionCount()) -> messagesListener.onMessageReactionCount(update.messageReactionCount())
        else -> AppLogger.i("unknown update: $update")
    }

    private fun exist(any: Any?) = any != null
}
