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
     * 			"Hello! ",
     * 			"Здравствуйте! ",
     * 			null,
     * 			null,
     * 			10
     * 		],
     * 		[
     * 			"My name is Uncle Vasya! ",
     * 			"Меня зовут Дядя Вася! ",
     * 			null,
     * 			null,
     * 			3,
     * 			null,
     * 			null,
     * 			[
     * 				[]
     * 			],
     * 			[
     * 				[
     * 					[
     * 						"e38d47a4f17c466dfca5ed67f09b5027",
     * 						"ru_en_2023q1.md"
     * 					]
     * 				]
     * 			]
     * 		]
     * 	],
     * 	null,
     * 	"ru",
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
