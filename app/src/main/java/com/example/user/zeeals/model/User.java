package com.example.user.zeeals.model;

import java.util.ArrayList;

public class User {


    private String access_token;
    private String token_type;
    private String id;
    private String full_name;
    private String email;
    private String phone;
    private String address;
    private int tour_flag;
    private int status;
    private String email_verified_at;
    private String created_at;
    private String updated_at;
    private String deleted_at;
    private boolean is_verified;
    private Url[] url;


    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }
}


