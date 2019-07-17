package com.example.user.zeeals.service;

import com.example.user.zeeals.ResponsesAndRequest.Account_id;
import com.example.user.zeeals.ResponsesAndRequest.updateUser_Model;
import com.example.user.zeeals.model.ForgotPassword;
import com.example.user.zeeals.ResponsesAndRequest.Basic_Response;
import com.example.user.zeeals.ResponsesAndRequest.ChangePassword_Model;
import com.example.user.zeeals.ResponsesAndRequest.IconList;
import com.example.user.zeeals.model.Login;
import com.example.user.zeeals.ResponsesAndRequest.Message;
import com.example.user.zeeals.model.AuthLogin;
import com.example.user.zeeals.ResponsesAndRequest.PostGroupResponse;
import com.example.user.zeeals.model.Registration;
import com.example.user.zeeals.model.zGroup;
import com.example.user.zeeals.model.zGroupList;
import com.example.user.zeeals.model.zSource;
import com.example.user.zeeals.ResponsesAndRequest.PostLinkResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface UserClient {

    /**
     * API FOR AUTHENTICATIONS
     */

    @POST("api/v1/auth/login")
    Call<AuthLogin> login (@Body Login login);

    @GET("api/v1/credential")
    Call<ResponseBody> getLink(@Header("Authorization") String token);

    @POST("api/v1/auth/register")
    Call<Message> registration (@Body Registration registration);

    @POST("api/v1/auth/forgotpassword")
    Call<String> forgot(@Body ForgotPassword forgotPassword);


    /**
     *  API FOR GROUPS
     */

    /*Create Group*/
    @POST("api/v1/link/group")
    Call<PostGroupResponse> create(@Header("Authorization") String token, @Body zGroup zgroup);

    /*Get List Group*/
    @GET ("api/v1/link/group/")
    Call<zGroupList> showGroup (@Header("Authorization") String token);

    /*delete Group*/
    @DELETE ("api/v1/link/group/delete")
    Call<ResponseBody> delete(@Header("Authorization") String token,@Body zGroup zGroup);

    /* edit Group*/
    @PUT ("api/v1/link/group/update")
    Call<ResponseBody> update(@Header ("Authorization")String token,@Body zGroup zgroup);

    /*show all account group and link */
    @POST ("api/v1/links")
    Call<zGroupList> links(@Header ("Authorization") String token,@Body Account_id acid);

    /**
     * API FOR LINKS & ICON
     */
    /* Retreive Icon */
    @GET ("api/v1/icon")
    Call<IconList> icon(@Header("Authorization") String token);

    /*Create Link*/
    @POST("api/v1/link")
    Call<PostLinkResponse> createLink(@Header("Authorization") String token, @Body zSource zSource);

    /*Update Link*/
    @PUT("api/v1/link/update")
    Call<zSource> updateLink(@Header("Authorization") String token, @Body zSource zSource);

    /*Delete Link*/
    @DELETE("api/v1/link/delete")
    Call<ResponseBody> deleteLink(@Header("Authorization") String token, @Body zSource zSource);

    
    @HTTP(method = "DELETE",path = "api/v1/link/delete", hasBody = true)
    Call<ResponseBody> deleteLink2(@Header("Authorization") String token, @Body zSource zSource);
    /**
     * USER
     */
    @PUT("api/v1/user/changepassword")
    Call<Basic_Response> changepassword(@Header("Authorization") String token, @Body ChangePassword_Model ps);

    @PUT("api/v1/user/update")
    Call<Basic_Response> userUpdate(@Header("Authorization") String token, @Body updateUser_Model um);

    @POST("api/v1/upload/avatar")
    Call<ResponseBody> uploadProfile(@Header("Authorization")String token, @Body RequestBody file);

    @POST("api/v1/upload/banner")
    Call<ResponseBody> uploadBanner(@Header("Authorization")String token, @Body RequestBody file);

}
