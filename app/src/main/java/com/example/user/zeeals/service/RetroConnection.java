package com.example.user.zeeals.service;

import android.content.SharedPreferences;

import com.example.user.zeeals.util.ServerAPI;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroConnection {

    Retrofit retrofit;

    public RetroConnection() {
        this.retrofit = new Retrofit.Builder().baseUrl(ServerAPI.zeealseRESTAPI).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public UserClient getConnection(){
        UserClient userClient = retrofit.create(UserClient.class);
        return userClient;
    }

}
