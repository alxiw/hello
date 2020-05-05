package io.github.alxiw.icq.geekbot.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Service {

    @GET("api?format=json")
    Call<Joke> getJoke();
}
