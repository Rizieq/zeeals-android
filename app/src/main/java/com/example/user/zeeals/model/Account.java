package com.example.user.zeeals.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Account {
    @SerializedName("account_id")
    @Expose
    private Integer accountId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("business_cd")
    @Expose
    private Object businessCd;
    @SerializedName("main_url")
    @Expose
    private String mainUrl;
    @SerializedName("old_main_url")
    @Expose
    private String oldMainUrl;
    @SerializedName("account_name")
    @Expose
    private String accountName;
    @SerializedName("account_desc")
    @Expose
    private String accountDesc;
    @SerializedName("avatar")
    @Expose
    private Object avatar;
    @SerializedName("avatar_type")
    @Expose
    private Object avatarType;
    @SerializedName("banner")
    @Expose
    private Object banner;
    @SerializedName("banner_type")
    @Expose
    private Object bannerType;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Object getBusinessCd() {
        return businessCd;
    }

    public void setBusinessCd(Object businessCd) {
        this.businessCd = businessCd;
    }

    public String getMainUrl() {
        return mainUrl;
    }

    public void setMainUrl(String mainUrl) {
        this.mainUrl = mainUrl;
    }

    public String getOldMainUrl() {
        return oldMainUrl;
    }

    public void setOldMainUrl(String oldMainUrl) {
        this.oldMainUrl = oldMainUrl;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountDesc() {
        return accountDesc;
    }

    public void setAccountDesc(String accountDesc) {
        this.accountDesc = accountDesc;
    }

    public Object getAvatar() {
        return avatar;
    }

    public void setAvatar(Object avatar) {
        this.avatar = avatar;
    }

    public Object getAvatarType() {
        return avatarType;
    }

    public void setAvatarType(Object avatarType) {
        this.avatarType = avatarType;
    }

    public Object getBanner() {
        return banner;
    }

    public void setBanner(Object banner) {
        this.banner = banner;
    }

    public Object getBannerType() {
        return bannerType;
    }

    public void setBannerType(Object bannerType) {
        this.bannerType = bannerType;
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

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }
}
