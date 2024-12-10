package io.github.alxiw.hello.api

import io.github.alxiw.hello.model.Language
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleService {

    @GET("translate_tts")
    fun convert(
        @Query("client") client: String = "tw-ob",
        @Query("tl") lang: String = Language.EN.code,
        @Query("q") query: String
    ): Call<ResponseBody>
}
