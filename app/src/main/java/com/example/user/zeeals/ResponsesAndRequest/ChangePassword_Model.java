package com.example.user.zeeals.ResponsesAndRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePassword_Model {
    @SerializedName("old_pass")
    @Expose
    private String oldPass;
    @SerializedName("new_pass")
    @Expose
    private String newPass;

    public ChangePassword_Model(String oldPass, String newPass) {
        this.oldPass = oldPass;
        this.newPass = newPass;
    }
}
