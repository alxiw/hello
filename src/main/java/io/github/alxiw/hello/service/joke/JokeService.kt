package io.github.alxiw.hello.service;

import io.github.alxiw.hello.data.JokeDaoImpl;
import io.github.alxiw.hello.model.Joke;

import java.util.List;
import java.util.Random;

public class JokeService {

    private static volatile JokeService instance;

    public static JokeService getInstance() {
        // Первый проверка (не блокирующая)
        if (instance == null) {
            synchronized (JokeService.class) {
                // Второй проверка (блокирующая)
                if (instance == null) {
                    instance = new JokeService();
                }
            }
        }
        return instance;
    }

    private final JokeDao jokeDao;

    private final Random random = new Random();
    private final List<Joke> jokes;

    private JokeService() {
        this.jokeDao = new JokeDaoImpl();
        this.jokes = jokeDao.getAllJokes();
    }

    public Joke getRandomJoke() {
        if (jokes == null || jokes.isEmpty()) {
            return null;
        }
        int index = random.nextInt(jokes.size());
        return jokes.get(index);
    }
}
