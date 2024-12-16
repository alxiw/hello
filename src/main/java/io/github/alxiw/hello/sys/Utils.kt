package io.github.alxiw.hello.sys

import com.pengrad.telegrambot.model.Chat
import io.github.alxiw.hello.sys.Language.Companion.fromCode

object Utils {

    fun checkLanguage(cmd: String): Language? {
        when (cmd.lowercase()) {
            "" -> return Language.EN
            "uk" -> return Language.UK
            "us" -> return Language.US
        }

        return fromCode(cmd)
    }

    fun makeUpName(chat: Chat): String {
        val first = chat.firstName()
        val last = chat.lastName()
        if (first.isNullOrEmpty() && last.isNullOrEmpty()) {
            return ""
        }
        if (last.isNullOrEmpty()) {
            return first
        }
        if (first.isNullOrEmpty()) {
            return last
        }

        return "$first $last"
    }

    fun getDatabaseResponse(value: Int): Response {
        return if (value <= -1) {
            return Response.ERROR
        } else if (value == 0) {
            return Response.NO_CHANGES
        } else {
            return Response.SUCCESS
        }
    }

    fun parseTranslateResponse(responseString: String): Pair<String, String>? {
        if (responseString.startsWith("[[[")) {
            val array = responseString.replaceFirst("[[[", "").split("\",")
            val target = array[0].replaceFirst("\"", "").replace("\\\"", "\"")
            val source = array[1].replaceFirst("\"", "")
            return Pair<String, String>(source, target)
        } else {
            return null
        }
    }
}
