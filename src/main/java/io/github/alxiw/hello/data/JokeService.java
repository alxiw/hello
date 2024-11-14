package io.github.alxiw.hello.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.alxiw.hello.sys.AppLogger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

public class JokeService {

    private final Random random = new Random();
    private List<Joke> jokes = null;

    public JokeService() {
        loadJokes();
    }

    private void loadJokes() {
        Gson gson = new Gson();
        Type jokeListType = new TypeToken<List<Joke>>() {}.getType();
        try (InputStream inputStream = getClass().getResourceAsStream("/data.json")) {
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
