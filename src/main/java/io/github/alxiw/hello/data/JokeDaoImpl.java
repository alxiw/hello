package io.github.alxiw.hello.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.alxiw.hello.model.Joke;
import io.github.alxiw.hello.service.JokeDao;
import io.github.alxiw.hello.sys.AppLogger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import static io.github.alxiw.hello.data.Const.JSON_URL;

public class JokeDaoImpl implements JokeDao {

    @Override
    public List<Joke> getAllJokes() {
        Gson gson = new Gson();
        Type jokeListType = new TypeToken<List<Joke>>() {}.getType();
        try (InputStream inputStream = getClass().getResourceAsStream(JSON_URL)) {
            InputStreamReader reader;
            if (inputStream != null) {
                reader = new InputStreamReader(inputStream);
                return gson.fromJson(reader, jokeListType);
            }
        } catch (Exception e) {
            AppLogger.e(e, "error occurred while parsing jokes" + e.getMessage());
        }

        return null;
    }
}
