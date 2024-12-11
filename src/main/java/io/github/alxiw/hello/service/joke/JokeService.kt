package io.github.alxiw.hello.service.joke

import io.github.alxiw.hello.data.JokeDao
import io.github.alxiw.hello.data.model.Joke
import java.util.Random

class JokeService() {

    private val jokeDao: IJokeDao = JokeDao()

    private val random = Random()
    private val jokes = jokeDao.getAllJokes()

    fun getRandomJoke(): Joke? {
        if (jokes.isNullOrEmpty()) {
            return null
        }
        val index = random.nextInt(jokes.size)
        return jokes[index]
    }

    companion object {

        @Volatile
        private var instance: JokeService? = null

        @JvmStatic
        fun getInstance(): JokeService {
            return instance ?: JokeService().also { instance = it }
        }
    }
}
