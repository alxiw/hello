package io.github.alxiw.hello.core

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.UpdatesListener
import com.pengrad.telegrambot.model.Update
import io.github.alxiw.hello.sys.AppLogger

class UpdateRouter(bot: TelegramBot) : UpdatesListener {

    private val listener = MessageController(bot)

    override fun process(updates: MutableList<Update>): Int {
        AppLogger.i("updates received, size: " + updates.size)
        updates.forEach { update -> accept(update, listener) }

        return UpdatesListener.CONFIRMED_UPDATES_ALL
    }

    private fun accept(update: Update, messageController: MessageController) = when {
        exist(update.message()) -> messageController.onNewMessage(update.message())
        exist(update.editedMessage()) -> messageController.onEditedMessage(update.message())
        exist(update.messageReaction()) -> messageController.onMessageReaction(update.messageReaction())
        exist(update.messageReactionCount()) -> messageController.onMessageReactionCount(update.messageReactionCount())
        else -> AppLogger.i("unknown update: $update")
    }

    private fun exist(any: Any?) = any != null
}
