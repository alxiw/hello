package io.github.alxiw.hello.service.google

import io.github.alxiw.hello.sys.AppLogger
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Logger
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://translate.google.com/"

object ServiceProvider {

    private var service: GoogleService? = null

    fun getService(): GoogleService {
        return service ?: provideService().also { service = it }
    }

    private fun provideService(): GoogleService {
        val interceptor = provideHttpLoggingInterceptor()
        val client = provideClient(interceptor)
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleService::class.java)
    }

    private fun provideClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(object : Logger {
            override fun log(message: String) {
                AppLogger.i(message)
            }
        }).apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
    }
}
