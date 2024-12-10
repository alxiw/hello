package io.github.alxiw.hello.core

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.MessageReactionCountUpdated
import com.pengrad.telegrambot.model.MessageReactionUpdated
import com.pengrad.telegrambot.model.Sticker
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.request.SendSticker
import com.pengrad.telegrambot.request.SendVoice
import com.pengrad.telegrambot.response.SendResponse
import io.github.alxiw.hello.api.GoogleService
import io.github.alxiw.hello.api.ServiceProvider
import io.github.alxiw.hello.data.Const.DATA_DIR
import io.github.alxiw.hello.data.Const.TEMP_FILE_EXT
import io.github.alxiw.hello.service.account.AccountService
import io.github.alxiw.hello.service.joke.JokeService
import io.github.alxiw.hello.service.Response
import io.github.alxiw.hello.service.sticker.StickerService
import io.github.alxiw.hello.model.Language
import io.github.alxiw.hello.sys.AppLogger
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MessagesListener(private val bot: TelegramBot) {

    private val jokeService: JokeService = JokeService()
    private val accountService: AccountService = AccountService.getInstance()
    private val stickerService: StickerService = StickerService.getInstance()
    private val googleService: GoogleService = ServiceProvider.getService()

    fun onNewMessage(message: Message) {
        val messageId = message.messageId().toLong()
        val chatId = message.chat().id()
        val name = getName(message.chat().firstName(), message.chat().lastName())
        AppLogger.i("new message, chat: ${message.chat().id()}")

        if (
            accountService.allAccounts.none { account -> account.uin == chatId.toString() } &&
            accountService.addAccount(chatId.toString(), name) == Response.SUCCESS
        ) {
            AppLogger.i("account with id $chatId added to database")
        }

        val text = message.text()
        if (message.sticker() != null) {
            val sticker = message.sticker()
            AppLogger.i("received message $messageId has sticker: ${sticker.fileId()}")
            onSticker(sticker, messageId, chatId)
            return
        }

        if (text != null && !text.isEmpty()) {
            AppLogger.i("received message $messageId has text: $text")
            if (text.startsWith("/start")) {
                onStartCommand(message)
                return
            }

            if (text.startsWith("$")) {
                val cmd = text.split(" ")[0]
                checkLanguage(cmd)?.let {
                    val content = text.replace(cmd, "").trim()
                    onTextToSpeech(content, it, messageId, chatId)
                    return
                }
            }

            onTextMessage(messageId, chatId)
            return
        }

        AppLogger.i("received message has empty content")
    }

    fun onEditedMessage(message: Message) {
        AppLogger.i("EditedMessageEvent, chat:" + message.chat().id())
    }

    fun onMessageReaction(messageReactionUpdated: MessageReactionUpdated) {
        AppLogger.i("MessageReactionUpdated, chat:" + messageReactionUpdated.chat().id())
    }

    fun onMessageReactionCount(messageReactionCountUpdated: MessageReactionCountUpdated) {
        AppLogger.i("MessageReactionCountUpdated, chat:" + messageReactionCountUpdated.chat().id())
    }

    private fun onStartCommand(message: Message) {
        val messageId = message.messageId().toLong()
        val chatId = message.chat().id()
        val author = getName(message.chat().firstName(), message.chat().lastName())
        val text = "*Hi $author!*\n\nI can currently do 3 things:\n\n" +
                "*Text-to-speech:* Send me a message starting with \$ (e.g., \$ hello world) and I'll read it " +
                "aloud. The default language is English, but you can specify a different language by adding a " +
                "two-letter language code after \$ (e.g., \$ru доброго здравия).\n\n*Jokes:* For any other text " +
                "message, I'll send you a random joke from my database.\n\n*Stickers:* If you send me a sticker, " +
                "I'll reply with a random sticker from my database.\n\n*P.S.* Supported languages: en or uk – " +
                "English UK (or by default without suffix), us – English US, ru – Russian, de – German, fr – " +
                "French, es – Spanish, it – Italian, pt – Portuguese, nl – Dutch, sv – Swedish, pl – Polish, " +
                "cs – Czech, ar – Arabic, tr – Turkish, zh – Simplified Chinese, ja – Japanese. \n"
        val message = SendMessage(chatId, text)
        message.parseMode(ParseMode.Markdown)
        val response = bot.execute<SendMessage, SendResponse>(message)
        AppLogger.i("reply to message with id " + messageId + " is hello message, response is ok – " + response.isOk)
        return
    }

    private fun onTextMessage(messageId: Long, chatId: Long) {
        val joke = jokeService.getRandomJoke() ?: return

        val id = joke.id
        val original = joke.original
        val russian = joke.russian
        val interpretation = joke.interpretation

        val reply = "*№$id*\n\n`\n$original\n`\n`\n$russian\n`\n\n$interpretation"
        val message = SendMessage(chatId, reply)
        message.parseMode(ParseMode.Markdown)
        val response = bot.execute<SendMessage, SendResponse>(message)
        AppLogger.i("reply to message with id " + messageId + " is joke with id " + id + ", response is ok – " + response.isOk)
    }

    private fun onSticker(sticker: Sticker, messageId: Long, chatId: Long) {
        val fileId = sticker.fileId()
        if (stickerService.allStickers.stream()
                .noneMatch { item: io.github.alxiw.hello.model.Sticker? -> item!!.fileId == fileId }
            && stickerService.addSticker(fileId, sticker.emoji()) == Response.SUCCESS
        ) {
            AppLogger.i("sticker with id $fileId added to database")
        }

        val id = stickerService.getRandomSticker(fileId)
        val response = bot.execute<SendSticker, SendResponse>(SendSticker(chatId, id))
        AppLogger.i("reply to message with id " + messageId + " is sticker with id " + id + ", response is ok – " + response.isOk)
    }

    private fun onTextToSpeech(content: String, lang: Language, messageId: Long, chatId: Long) {
        if (content.isBlank()) {
            val text = "\uD83E\uDEE5 Message for translation is empty"
            val message = SendMessage(chatId, text)
            val response = bot.execute<SendMessage, SendResponse>(message)
            AppLogger.i("reply to message with id " + messageId + ", response is ok – " + response.isOk)
            return
        }

        val request = googleService.convert(lang = lang.code, query = content)
        val response = request.execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            val tempFile = File("$DATA_DIR${messageId}$TEMP_FILE_EXT")
            try {
                body.use {
                    FileOutputStream(tempFile).use { outputStream -> IOUtils.copy(it.byteStream(), outputStream) }
                    val message = SendVoice(chatId, tempFile)
                    val resp = bot.execute(message)

                    AppLogger.i("reply to message with id $messageId is voice message, response is ok – " + resp.isOk)
                }
            } catch (e: IOException) {
                AppLogger.i("error during file handling: ${e.message}")

                val text = "\uD83D\uDC7E Oops, error, voice message file is corrupted..."
                val message = SendMessage(chatId, text)
                val resp = bot.execute(message)

                AppLogger.i("reply to message with id $messageId is error text message, response is ok – " + resp.isOk)
            } finally {
                if (tempFile.exists()) {
                    tempFile.delete()
                    AppLogger.i("temporary file deleted successfully")
                }
            }
        } else {
            val text = "\uD83D\uDC7E Oops, error, most likely the text exceeds 100-character limit..."
            val message = SendMessage(chatId, text)
            val resp = bot.execute(message)

            AppLogger.i("reply to message with id $messageId is error text message, response is ok – " + resp.isOk)
        }
    }

    private fun checkLanguage(cmd: String): Language? {
        when (cmd) {
            "$" -> return Language.EN
            "\$uk" -> return Language.UK
            "\$us" -> return Language.US
            "$${Language.RU.code}" -> return Language.RU
            "$${Language.NL.code}" -> return Language.NL
            "$${Language.EN.code}" -> return Language.EN
            "$${Language.FR.code}" -> return Language.FR
            "$${Language.DE.code}" -> return Language.DE
            "$${Language.IT.code}" -> return Language.IT
            "$${Language.CS.code}" -> return Language.CS
            "$${Language.PL.code}" -> return Language.PL
            "$${Language.ES.code}" -> return Language.ES
            "$${Language.TR.code}" -> return Language.TR
            "$${Language.PT.code}" -> return Language.PT
            "$${Language.ZH.code}" -> return Language.ZH
            "$${Language.AR.code}" -> return Language.AR
            "$${Language.SV.code}" -> return Language.SV
            "$${Language.JA.code}" -> return Language.JA
        }

        return null
    }

    private fun getName(first: String, last: String): String {
        if (first.isEmpty() && last.isEmpty()) {
            return ""
        }
        if (last.isEmpty()) {
            return first
        }
        if (first.isEmpty()) {
            return last
        }

        return "$first $last"
    }
}
