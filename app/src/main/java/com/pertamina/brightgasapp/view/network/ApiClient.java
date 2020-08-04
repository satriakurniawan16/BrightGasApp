package com.pertamina.brightgasapp.view.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "https://apps.pertamina.com/BGAPI/";
    private static final String BASE_URL_GLIGGY = "https://api.brightgas.co.id/";
    private static final String BASE_URL_MUSICOOL = "https://api.musicoolpromo.com/";

    private static Retrofit retrofit = null;
    private static Retrofit secondretrofit = null;
    private static Retrofit thirdretrofit = null;

    public static Retrofit getData(){

        OkHttpClient newClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .callTimeout(30, TimeUnit.SECONDS)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(newClient)
                    .build();
        }

        return retrofit;

    }

    public static Retrofit getDataMusicool(){

        OkHttpClient mClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        if(secondretrofit == null){
            secondretrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_MUSICOOL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(mClient)
                    .build();
        }
        return secondretrofit;
    }

    public static Retrofit getDataBrightGas(){

        OkHttpClient mClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        if(thirdretrofit == null){
            thirdretrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_GLIGGY)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(mClient)
                    .build();
        }
        return thirdretrofit;
    }

}
