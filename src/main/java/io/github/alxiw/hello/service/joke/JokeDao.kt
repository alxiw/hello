package io.github.alxiw.hello.service.joke

import io.github.alxiw.hello.model.Joke

interface JokeDao {
    fun getAllJokes(): List<Joke>?
}
