package io.github.alxiw.hello.sys

import com.pengrad.telegrambot.model.Chat
import io.github.alxiw.hello.sys.Language.Companion.fromCode

object Utils {

    fun checkLanguage(cmd: String): Language? {
        when (cmd.lowercase()) {
            "$" -> return Language.EN
            "\$uk" -> return Language.UK
            "\$us" -> return Language.US
        }

        return fromCode(cmd.replace("$", ""))
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
}
