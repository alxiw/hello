package io.github.alxiw.icq.geekbot.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceProvider {

    private static final String BASE_URL = "https://geek-jokes.sameerkumar.website/";
    private static Service service;

    private ServiceProvider() {}

    public static Service getService(){
        if (service == null) {
            service = provideService();
        }
        return service;
    }

    private static Service provideService() {
        HttpLoggingInterceptor interceptor = provideInterceptor();
        OkHttpClient client = provideClient(interceptor);
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Service.class);
    }

    private static OkHttpClient provideClient(HttpLoggingInterceptor interceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }

    private static HttpLoggingInterceptor provideInterceptor() {
        return new HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
                .setLevel(HttpLoggingInterceptor.Level.BASIC);
    }
}
