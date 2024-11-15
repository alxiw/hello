package io.github.alxiw.hello.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.alxiw.hello.model.Joke;
import io.github.alxiw.hello.sys.AppLogger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

import static io.github.alxiw.hello.data.Constants.JSON_URL;

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

    private final Random random = new Random();
    private List<Joke> jokes = null;

    private JokeService() {
        loadJokes();
    }

    private void loadJokes() {
        Gson gson = new Gson();
        Type jokeListType = new TypeToken<List<Joke>>() {}.getType();
        try (InputStream inputStream = getClass().getResourceAsStream(JSON_URL)) {
            InputStreamReader reader;
            if (inputStream != null) {
                reader = new InputStreamReader(inputStream);
                jokes = gson.fromJson(reader, jokeListType);
            }
        } catch (Exception e) {
            AppLogger.e(e, "error occurred while parsing jokes" + e.getMessage());
        }
    }

    public Joke getRandomJoke() {
        if (jokes == null || jokes.isEmpty()) {
            return null;
        }
        int index = random.nextInt(jokes.size());
        return jokes.get(index);
    }
}
