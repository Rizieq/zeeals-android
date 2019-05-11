package com.example.user.zeeals.ResponsesAndRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Basic_Response {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("serve")
    @Expose
    private Object serve;

    public String getMessage() {
        return message;
    }

    public Object getServe() {
        return serve;
    }
}
