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
import io.github.alxiw.hello.service.google.GoogleService
import io.github.alxiw.hello.service.google.ServiceProvider
import io.github.alxiw.hello.sys.Const.DATA_DIR
import io.github.alxiw.hello.sys.Const.TEMP_FILE_EXT
import io.github.alxiw.hello.service.account.AccountService
import io.github.alxiw.hello.service.joke.JokeService
import io.github.alxiw.hello.service.sticker.StickerService
import io.github.alxiw.hello.sys.Language
import io.github.alxiw.hello.sys.AppLogger
import io.github.alxiw.hello.sys.Utils.checkLanguage
import io.github.alxiw.hello.sys.Utils.makeUpName
import io.github.alxiw.hello.sys.Utils.parseTranslateResponse
import io.github.alxiw.hello.sys.isOk
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MessageController(private val bot: TelegramBot) {

    private val jokeService: JokeService = JokeService.getInstance()
    private val accountService: AccountService = AccountService.getInstance()
    private val stickerService: StickerService = StickerService.getInstance()
    private val googleService: GoogleService = ServiceProvider.getService()

    fun onNewMessage(message: Message) {
        val messageId = message.messageId().toLong()
        val chatId = message.chat().id()

        AppLogger.i("new message $messageId, chat $chatId")
        keepAccountIfNecessary(message)

        when {
            message.sticker() != null -> {
                val sticker = message.sticker()
                AppLogger.i("received message $messageId has sticker ${sticker.fileId()}")
                onSticker(sticker, messageId, chatId)
            }
            !message.text().isNullOrEmpty() -> {
                val text = message.text()
                AppLogger.i("received message $messageId has text – $text")
                when {
                    text.startsWith("/start") -> onStartCommand(message)
                    text.startsWith("$") -> onTranslate(text, messageId, chatId)
                    else -> onTextMessage(messageId, chatId)
                }
            }
            message.document() != null -> {
                AppLogger.i("received message has file ${message.document().fileId()}")
            }
            !message.photo().isNullOrEmpty() -> {
                AppLogger.i("received message has ${message.photo().size} photos")
            }
            message.animation() != null -> {
                AppLogger.i("received message has animation ${message.animation().fileId()}")
            }
            message.audio() != null -> {
                AppLogger.i("received message has audio ${message.audio().fileId}")
            }
            message.contact() != null -> {
                AppLogger.i("received message has contact ${message.contact()}")
            }
            else -> {
                AppLogger.i("received message has unknown content")
            }
        }
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

    private fun keepAccountIfNecessary(message: Message) {
        val chatId = message.chat().id()
        val hasNoAccount = accountService.getAllAccounts().none { account -> account.uin == chatId.toString() }
        if (hasNoAccount) {
            val name = makeUpName(message.chat())
            val resp = accountService.addAccount(chatId.toString(), name)
            if (resp.isOk()) AppLogger.i("account $chatId has kept in database")
        }
    }

    private fun keepStickerIfNecessary(sticker: Sticker) {
        val fileId = sticker.fileId()
        val hasNoSticker = stickerService.getAllStickers().none { sticker -> sticker.fileId == fileId }
        if (hasNoSticker) {
            val emoji = sticker.emoji()
            val resp = stickerService.addSticker(fileId, emoji)
            if (resp.isOk()) AppLogger.i("sticker with id $fileId and emoji $emoji added to database")
        }
    }

    private fun onStartCommand(message: Message) {
        val messageId = message.messageId().toLong()
        val chatId = message.chat().id()
        val author = makeUpName(message.chat())
        val text = "\uD83D\uDC4B *Hi $author!*\n\n" +
                "I can currently do 4 things:\n\n" +
                "\uD83D\uDCE2 *Text-to-speech:* " +
                "Send me a message starting with \$ (e.g., \$ hello world) and I'll read it " +
                "aloud. The default language is English, but you can specify a different language by adding a " +
                "two-letter language code after \$ (e.g., \$ru доброго здравия).\n\n" +
                "\uD83C\uDF0E *Translation:* " +
                "In a similar way send me a message starting with the following format: \"\$en-ru\". " +
                "This indicates that you want to translate from English to Russian " +
                "(e.g., \$en-ru the quick brown fox jumps over the lazy dog). " +
                "I will then provide you with the translation.\n\n" +
                "\uD83D\uDE02 *Jokes:* " +
                "For any other text message, I'll send you a random joke from my database.\n\n" +
                "\uD83E\uDE84 *Stickers:* " +
                "If you send me a sticker, I'll reply with a random sticker from my database.\n\n" +
                "*P.S.* \uD83D\uDCAC Supported languages: en or uk – English UK, " +
                "us – English US, ru – Russian, de – German, fr – French, es – Spanish, it – Italian, " +
                "pt – Portuguese, nl – Dutch, sv – Swedish, pl – Polish, cs – Czech, ar – Arabic, tr – Turkish, " +
                "zh – Simplified Chinese, ja – Japanese.\n"
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
        keepStickerIfNecessary(sticker)

        val respStickerId = stickerService.getRandomSticker(sticker.fileId())
        val response = bot.execute<SendSticker, SendResponse>(SendSticker(chatId, respStickerId))
        AppLogger.i("reply to message with id " + messageId + " is sticker with id " + respStickerId + ", response is ok – " + response.isOk)
    }

    private fun onTranslate(text: String, messageId: Long, chatId: Long) {
        val cmd = text.trim().split(" ")[0]
        val lang = cmd.replaceFirst("$", "")
        if (lang.contains("-")) {
            val pair = lang.split("-")
            if (pair.size != 2) {
                val text = "\uD83E\uDEE5 More than 2 language codes, please try again"
                val message = SendMessage(chatId, text)
                val response = bot.execute<SendMessage, SendResponse>(message)
                AppLogger.i("reply to message with id " + messageId + ", response is ok – " + response.isOk)
                return
            }
            val sl = checkLanguage(pair[0])
            val tl = checkLanguage(pair[1])
            if (sl == null || tl == null) {
                val text = "\uD83E\uDEE5 Language codes are incorrect or not supported, please try again"
                val message = SendMessage(chatId, text)
                val response = bot.execute<SendMessage, SendResponse>(message)
                AppLogger.i("reply to message with id " + messageId + ", response is ok – " + response.isOk)
                return
            }
            val content = text.replace(cmd, "").trim()
            translate(content, sl, tl, messageId, chatId)
        } else {
            checkLanguage(lang)?.let {
                val content = text.replace(cmd, "").trim()
                convertTextToSpeech(content, it, messageId, chatId)
            } ?: run {
                val text = "\uD83E\uDEE5 Language code is incorrect or not supported, please try another one"
                val message = SendMessage(chatId, text)
                val response = bot.execute<SendMessage, SendResponse>(message)
                AppLogger.i("reply to message with id " + messageId + ", response is ok – " + response.isOk)
            }
        }
    }

    private fun convertTextToSpeech(content: String, lang: Language, messageId: Long, chatId: Long) {
        if (content.isBlank()) {
            val text = "\uD83E\uDEE5 Message for translation is empty"
            val message = SendMessage(chatId, text)
            val response = bot.execute<SendMessage, SendResponse>(message)
            AppLogger.i("reply to message with id " + messageId + ", response is ok – " + response.isOk)
            return
        }

        val request = googleService.tts(lang = lang.code, query = content)
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

    private fun translate(content: String, source: Language, target: Language, messageId: Long, chatId: Long) {
        if (content.isBlank()) {
            val text = "\uD83E\uDEE5 Message for translation is empty"
            val message = SendMessage(chatId, text)
            val response = bot.execute<SendMessage, SendResponse>(message)
            AppLogger.i("reply to message with id " + messageId + ", response is ok – " + response.isOk)
            return
        }

        val request = googleService.translate(sourceLang = source.code, targetLang = target.code, query = content)
        request.execute().body()?.string()?.let { parseTranslateResponse(it) }?.let { pair ->
            val message = SendMessage(chatId, pair.second)
            val resp = bot.execute(message)
            AppLogger.i("reply to message with id $messageId is text message, response is ok – " + resp.isOk)
        } ?: run {
            val text = "\uD83D\uDC7E Oops, error, most likely the text exceeds limits..."
            val message = SendMessage(chatId, text)
            val resp = bot.execute(message)
            AppLogger.i("reply to message with id $messageId is error text message, response is ok – " + resp.isOk)
        }
    }
}
