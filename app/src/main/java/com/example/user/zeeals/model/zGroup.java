package com.example.user.zeeals.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.ArrayList;
import java.util.List;

public class zGroup extends Zlink {


    private boolean hasChild;
    private boolean childrenVisible;

        @SerializedName("group_link_id")
        @Expose
        private Integer groupLinkId;
        @SerializedName("account_id")
        @Expose
        private Integer accountId;
        @SerializedName("unicode")
        @Expose
        private String unicode;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("orientation")
        @Expose
        private char orientation;
        @SerializedName("position")
        @Expose
        private Integer position;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("uncategorized")
        @Expose
        private Integer uncategorized;
        @SerializedName("child_link")
        @Expose
        private List<zSource> childLink = null;




    @Override
    public boolean isParent() {
        return true;
    }


    public boolean isChildrenVisible() {
        return childrenVisible;
    }

    public void setChildrenVisible(boolean childrenVisible) {
        this.childrenVisible = childrenVisible;
    }

    public zGroup() {
    }

    public zGroup(int id, String title) {
        this.groupLinkId = id;
        this.title = title;
        this.hasChild=true;
    }

    public zGroup(boolean isHaveChild, Integer groupLinkId, Integer accountId, String unicode, String title, char orientation,  Integer status,  String updatedAt, Integer uncategorized, List<zSource> childLink) {
        this.groupLinkId = groupLinkId;
        this.accountId = accountId;
        this.unicode = unicode;
        this.title = title;
        this.orientation = orientation;
        this.status = status;
        this.updatedAt = updatedAt;
        this.uncategorized = uncategorized;
        this.childLink = childLink;
        hasChild=isHaveChild;
    }

    public zGroup(int id, ArrayList<zSource> zSource, String title) {
        this.groupLinkId = id;
        this.childLink = zSource;
        this.title= title;
        hasChild=false;
    }

    public zGroup(char orientation, String title, final String icon, int status) {
        this.orientation = orientation;
        this.title = title;
        this.unicode= icon;
        this.status = status;
        hasChild=true;
    }

    public zGroup(char orientation, String title, final String unicode, int status,int group_link_id) {
        this.groupLinkId = group_link_id;
        this.orientation = orientation;
        this.status=status;
        this.title = title;
        this.unicode = unicode;
        hasChild=true;
    }

    public boolean isHasChild() {
        if(childLink==null){
            return false;
        }else if(childLink.isEmpty()) return false;
        else return true;
    }


    public Integer getGroupLinkId() {
        return groupLinkId;
    }

    public void setGroupLinkId(Integer groupLinkId) {
        this.groupLinkId = groupLinkId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getUnicode() {
        return unicode;
    }

    public void setUnicode(String unicode) {
        this.unicode = unicode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public char getOrientation() {
        return orientation;
    }

    public void setOrientation(char orientation) {
        this.orientation = orientation;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
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

    public Integer getUncategorized() {
        return uncategorized;
    }

    public void setUncategorized(Integer uncategorized) {
        this.uncategorized = uncategorized;
    }

    public List<zSource> getChildLink() {
        return childLink;
    }

    public void setChildLink(List<zSource> childLink) {
        this.childLink = childLink;
    }
}
