package com.example.user.zeeals.model;

import com.example.user.zeeals.ResponsesAndRequest.Serve;

import java.util.List;

public class AuthLogin {
    private String message;
    private Serve serve;

    public AuthLogin() {
    }

    public Serve getServe() {
        return serve;
    }

    public User getUser(){return serve.getUser();}

    public List<Account> getAccount(){return serve.getUser().getAccount();}

}
