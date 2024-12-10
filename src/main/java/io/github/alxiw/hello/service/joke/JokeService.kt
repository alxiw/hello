package io.github.alxiw.hello.service.joke

import io.github.alxiw.hello.data.JokeDaoImpl
import io.github.alxiw.hello.model.Joke
import java.util.Random

class JokeService() {

    private val jokeDao: JokeDao = JokeDaoImpl()

    private val random = Random()
    private val jokes = jokeDao.getAllJokes()

    fun getRandomJoke(): Joke? {
        if (jokes.isNullOrEmpty()) {
            return null
        }
        val index = random.nextInt(jokes.size)
        return jokes[index]
    }
}
