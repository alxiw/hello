package io.github.alxiw.hello.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.alxiw.hello.model.Joke
import io.github.alxiw.hello.service.joke.JokeDao
import io.github.alxiw.hello.sys.AppLogger
import java.io.InputStreamReader
import java.lang.Exception

class JokeDaoImpl : JokeDao {

    override fun getAllJokes(): List<Joke>? {
        val gson = Gson()
        val jokeListType = object : TypeToken<MutableList<Joke?>?>() {}.type
        try {
            javaClass.getResourceAsStream(Const.JSON_URL).use { inputStream ->
                inputStream ?: return null
                return gson.fromJson<MutableList<Joke>>(InputStreamReader(inputStream), jokeListType)
            }
        } catch (e: Exception) {
            AppLogger.e(e, "error occurred while parsing jokes" + e.message)
        }

        return null
    }
}
