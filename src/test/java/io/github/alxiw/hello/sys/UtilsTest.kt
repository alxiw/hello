package io.github.alxiw.hello.sys

import com.pengrad.telegrambot.model.Chat
import org.mockito.Mockito
import kotlin.test.Test
import kotlin.test.junit.JUnitAsserter.assertEquals

class UtilsTest {

    @Test
    fun `check language`() {
        val inputs = listOf<Pair<String, Language?>>(
            Pair("\$ abc", Language.EN),
            Pair("\$", Language.EN),
            Pair("\$ ru", Language.EN),
            Pair("\$en", Language.EN),
            Pair("\$xz", null),
            Pair("__\$", null),
            Pair(" \$ ru", Language.EN),
            Pair("\$\$\$ hello", null),
            Pair("\$70", null),
            Pair("  .\$.  ", null),
            Pair("\$en-GB", null),
            Pair("\$en-US", null),
            Pair("\$uk", Language.UK),
            Pair("\$us", Language.US),
            Pair("\$de", Language.DE),
            Pair("\$pl", Language.PL),
            Pair("\$ja", Language.JA),
        )
        inputs.forEachIndexed { index, input ->
            val cmd = input.first.trim().split(" ")[0]
            val lang = Utils.checkLanguage(cmd)
            assertEquals("items indexed $index are not equals", input.second, lang)
        }
    }

    @Test
    fun `make up name`() {
        val names = listOf<Triple<String?, String?, String>>(
            Triple("A.", "I.", "A. I."),
            Triple("A.", null, "A."),
            Triple("A.", "", "A."),
            Triple(null, "I.", "I."),
            Triple("", "I.", "I."),
            Triple(null, null, ""),
            Triple("", "", ""),
        )
        names.forEachIndexed { index, name ->
            val chat = Mockito.mock(Chat::class.java)
            Mockito.`when`(chat.firstName()).thenReturn(name.first)
            Mockito.`when`(chat.lastName()).thenReturn(name.second)
            val result = Utils.makeUpName(chat)
            assertEquals("items indexed $index are not equals", name.third, result)
        }
    }

    @Test
    fun `get database response`() {
        val names = listOf<Pair<Int, Response>>(
            Pair(-100500, Response.ERROR),
            Pair(-2, Response.ERROR),
            Pair(-1, Response.ERROR),
            Pair(0, Response.NO_CHANGES),
            Pair(1, Response.SUCCESS),
            Pair(2, Response.SUCCESS),
            Pair(100500, Response.SUCCESS),
        )
        names.forEachIndexed { index, response ->
            val result = Utils.getDatabaseResponse(response.first)
            assertEquals("items indexed $index are not equals", response.second, result)
        }
    }
}