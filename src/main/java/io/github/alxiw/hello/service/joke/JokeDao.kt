package io.github.alxiw.hello.service;

import io.github.alxiw.hello.model.Joke;

import java.util.List;

public interface JokeDao {

    List<Joke> getAllJokes();
}