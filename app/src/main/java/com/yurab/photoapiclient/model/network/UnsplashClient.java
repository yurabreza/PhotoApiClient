package com.yurab.photoapiclient.model.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yurab.photoapiclient.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class UnsplashClient {

    private static final GsonBuilder gsonBuilder = new GsonBuilder();

    private static Gson gson = gsonBuilder.create();
    private static ApiInterface client = null;


    public static ApiInterface getApiInterface() {
        if (client != null) return client;
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        builder.client(getOkHttpClient());

        client = builder.build().create(ApiInterface.class);
        return client;
    }

    private static OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor httpInterceptor = new HttpLoggingInterceptor();
        httpInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(httpInterceptor)
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();
    }
}
