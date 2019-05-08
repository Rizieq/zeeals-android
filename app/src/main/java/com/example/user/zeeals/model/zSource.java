package com.example.user.zeeals.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class zSource extends Zlink {
    @SerializedName("link_id")
    @Expose
    private Integer linkId;
    @SerializedName("id")
    @Expose
    private Integer Id;
    @SerializedName("account_id")
    @Expose
    private Integer accountId;
    @SerializedName("group_link_id")
    @Expose
    private Integer groupLinkId;
    @SerializedName("link_key")
    @Expose
    private String linkKey;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("position")
    @Expose
    private Integer position;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("orientation")
    @Expose
    private String orientation;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;



    public zSource(Integer accountId, Integer groupLinkId, String linkKey, String url, String title, Integer position, String avatar, String orientation, Integer status) {
        this.accountId = accountId;
        this.groupLinkId = groupLinkId;
        this.linkKey = linkKey;
        this.url = url;
        this.title = title;
        this.position = position;
        this.avatar = avatar;
        this.orientation = orientation;
        this.status = status;
    }

    @Override
    public boolean isParent() {
        return false;
    }


    public Integer getId() {
        return Id;
    }

    public void setId() {
        Id = this.linkId;
    }
    public Integer getLinkId() {
        return linkId;
    }

    public void setLinkId(Integer linkId) {
        this.linkId = linkId;
        this.Id = linkId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getGroupLinkId() {
        return groupLinkId;
    }

    public void setGroupLinkId(Integer groupLinkId) {
        this.groupLinkId = groupLinkId;
    }

    public String getLinkKey() {
        return linkKey;
    }

    public void setLinkKey(String linkKey) {
        this.linkKey = linkKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
