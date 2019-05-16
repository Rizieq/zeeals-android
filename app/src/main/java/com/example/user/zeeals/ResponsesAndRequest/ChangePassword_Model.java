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
    @SerializedName("mobile")
    @Expose
    private String mobile;

    public ChangePassword_Model(String oldPass, String newPass) {
        this.oldPass = oldPass;
        this.newPass = newPass;
        this.mobile = "$2y$10$s6ZYEuThB8IkZ0sl1ucOOeJFYf/4DmGNbeIyB6j4l9lPpwdu41n5K";
    }
}
