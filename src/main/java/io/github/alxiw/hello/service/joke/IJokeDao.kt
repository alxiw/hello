package io.github.alxiw.hello.service.joke

import io.github.alxiw.hello.data.model.Joke

interface IJokeDao {
    fun getAllJokes(): List<Joke>?
}
