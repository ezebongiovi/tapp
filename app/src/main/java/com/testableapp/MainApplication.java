package com.testableapp;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainApplication extends Application {

    private static final String EMULATOR_URL = "http://10.0.2.2:8080/api/";
    private static final String TEST_URL = "http://localhost:8080/api/";

    private final static Retrofit mRetrofit = new Retrofit.Builder()
            .baseUrl(EMULATOR_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()))
            .client(getClient())
            .build();

    public static Retrofit getRetrofit() {
        return mRetrofit;
    }

    @NonNull
    public static OkHttpClient getClient() {
        return new OkHttpClient.Builder().addInterceptor(new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)).build();
    }
}
