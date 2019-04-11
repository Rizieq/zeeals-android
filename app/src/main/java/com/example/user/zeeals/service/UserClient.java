package com.example.user.zeeals.service;

import com.example.user.zeeals.model.ForgotPassword;
import com.example.user.zeeals.model.Login;
import com.example.user.zeeals.model.Message;
import com.example.user.zeeals.model.Registration;
import com.example.user.zeeals.model.User;
import com.example.user.zeeals.model.zGroup;
import com.example.user.zeeals.model.zGroupList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserClient {


    @POST("api/v1/auth/login")
    Call<User> login (@Body Login login);

    @GET("api/v1/link")
    Call<ResponseBody> getLink(@Header("Authorization") String token);

    @POST("api/v1/auth/register")
    Call<Message> registration (@Body Registration registration);

    @POST("api/v1/auth/forgotpassword")
    Call<String> forgot(@Body ForgotPassword forgotPassword);

    //Create Group
    @POST("api/v1/link/group/")
    Call<zGroup> create(@Header("Authorization") String token, @Body zGroup zgroup);

    //Get List Group
    @GET ("api/v1/link/group/")
    Call<zGroupList> showGroup (@Header("Authorization") String token);

    //delete Group
    @DELETE ("api/v1/link/group/{id}")
    Call<ResponseBody> delete(@Header("Authorization") String token, @Path("id") int id);


}
