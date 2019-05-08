package com.example.user.zeeals.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class zGroupList {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("serve")
    @Expose
    private List<zGroup> serve = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<zGroup> getServe() {
        return serve;
    }

    public void setServe(List<zGroup> serve) {
        this.serve = serve;
    }






}
