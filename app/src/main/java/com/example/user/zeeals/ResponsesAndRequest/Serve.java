package com.example.user.zeeals.ResponsesAndRequest;

import com.example.user.zeeals.model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Serve {
    @SerializedName("access_token")
    @Expose
    private String access_token;
    @SerializedName("user")
    @Expose
    private User user;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccessToken(String accessToken) {
        this.access_token = accessToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
