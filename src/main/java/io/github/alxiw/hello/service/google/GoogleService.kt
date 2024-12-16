package io.github.alxiw.hello.service.google

import io.github.alxiw.hello.sys.Language
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleService {

    @GET("translate_tts")
    fun tts(
        @Query("client") client: String = "tw-ob",
        @Query("tl") lang: String = Language.EN.code,
        @Query("q") query: String
    ): Call<ResponseBody>

    /**
     * [
     * 	[
     * 		[
     * 			"кот",
     * 			"cat",
     * 			null,
     * 			null,
     * 			10
     * 		]
     * 	],
     * 	null,
     * 	"en",
     * 	null,
     * 	null,
     * 	null,
     * 	null,
     * 	[]
     * ]
     */
    @GET("translate_a/single")
    fun translate(
        @Query("client") client: String = "gtx",
        @Query("sl") sourceLang: String = Language.EN.code,
        @Query("tl") targetLang: String = Language.RU.code,
        @Query("dt") dataType: String = "t",
        @Query("q") query: String
    ): Call<ResponseBody>
}
