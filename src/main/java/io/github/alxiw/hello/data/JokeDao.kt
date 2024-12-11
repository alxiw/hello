package io.github.alxiw.hello.data

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import io.github.alxiw.hello.data.model.Joke
import io.github.alxiw.hello.service.joke.IJokeDao
import io.github.alxiw.hello.sys.AppLogger
import io.github.alxiw.hello.sys.Const
import java.io.InputStreamReader

class JokeDao : IJokeDao {

    override fun getAllJokes(): List<Joke>? {
        val gson = Gson()
        val jokeListType = object : TypeToken<List<Joke>>() {}.type
        javaClass.getResourceAsStream(Const.JSON_URL).use { inputStream ->
            inputStream ?: return null
            try {
                return gson.fromJson<List<Joke>>(InputStreamReader(inputStream), jokeListType)
            } catch (e: JsonParseException) {
                AppLogger.e(e)
            }
        }

        return null
    }
}
