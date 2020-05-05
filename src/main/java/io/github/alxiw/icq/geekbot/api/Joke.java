package io.github.alxiw.icq.geekbot.api;

import com.google.gson.annotations.SerializedName;

public class Joke {

    @SerializedName("joke")
    private final String joke;

    public Joke(String joke) {
        this.joke = joke;
    }

    public String getJoke() {
        return joke;
    }
}
