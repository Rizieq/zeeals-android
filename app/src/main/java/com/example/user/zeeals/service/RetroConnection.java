package com.example.user.zeeals.service;

import android.content.SharedPreferences;

import com.example.user.zeeals.util.ServerAPI;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroConnection {

    Retrofit retrofit;

    public RetroConnection() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(logging).build();
//
//        httpClient.connectTimeout(100, TimeUnit.SECONDS);
//        httpClient.readTimeout(100,TimeUnit.SECONDS);
//        httpClient.writeTimeout(100, TimeUnit.SECONDS);
//        httpClient.addInterceptor(logging);
        this.retrofit = new Retrofit.Builder().baseUrl(ServerAPI.zeealseRESTAPI).addConverterFactory(GsonConverterFactory.create()).client(httpClient).build();
    }

    public UserClient getConnection(){
        UserClient userClient = retrofit.create(UserClient.class);
        return userClient;
    }

}
