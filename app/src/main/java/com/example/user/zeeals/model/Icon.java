package com.example.user.zeeals.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Icon {
    @SerializedName("sys_cat")
    @Expose
    private String sysCat;
    @SerializedName("sys_sub_cat")
    @Expose
    private String sysSubCat;
    @SerializedName("sys_cd")
    @Expose
    private String sysCd;
    @SerializedName("sys_val")
    @Expose
    private String sysVal;
    @SerializedName("sys_desc")
    @Expose
    private String sysDesc;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public String getSysCat() {
        return sysCat;
    }

    public void setSysCat(String sysCat) {
        this.sysCat = sysCat;
    }

    public String getSysSubCat() {
        return sysSubCat;
    }

    public void setSysSubCat(String sysSubCat) {
        this.sysSubCat = sysSubCat;
    }

    public String getSysCd() {
        return sysCd;
    }

    public void setSysCd(String sysCd) {
        this.sysCd = sysCd;
    }

    public String getSysVal() {
        return sysVal;
    }

    public void setSysVal(String sysVal) {
        this.sysVal = sysVal;
    }

    public String getSysDesc() {
        return sysDesc;
    }

    public void setSysDesc(String sysDesc) {
        this.sysDesc = sysDesc;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
