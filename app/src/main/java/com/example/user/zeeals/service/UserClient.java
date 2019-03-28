package com.example.user.zeeals.service;

import com.example.user.zeeals.model.ForgotPassword;
import com.example.user.zeeals.model.Login;
import com.example.user.zeeals.model.Message;
import com.example.user.zeeals.model.Registration;
import com.example.user.zeeals.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserClient {

    @POST("api/v1/auth/login")
    Call<User> login (@Body Login login);

    @GET("api/v1/link")
    Call<ResponseBody> getLink(@Header("Authorization") String token);

    @POST("api/v1/auth/register")
    Call<Message> registration (@Body Registration registration);

    @POST("api/v1/auth/forgotpassword")
    Call<String> forgot(@Body ForgotPassword forgotPassword);


}
