package com.example.user.zeeals.ResponsesAndRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class updateUser_Model {
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phone")
    @Expose
    private String phone;

    public updateUser_Model(String fullName, String address, String phone) {
        this.fullName = fullName;
        this.address = address;
        this.phone = phone;
    }
}
