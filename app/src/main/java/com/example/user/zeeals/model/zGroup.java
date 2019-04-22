package com.example.user.zeeals.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class zGroup extends Zlink implements Parcelable {


    private ArrayList<zSource> zSource;
    private boolean hasNoChild;
    private boolean childrenVisible;

    private int group_link_id;
    private int account_id;
    private char orientation;
    private String title;
    private String unicode;
    private int position;
    private int status;
    private String created_at;
    private String updated_at;

    protected zGroup(Parcel in) {
        group_link_id = in.readInt();
        account_id=in.readInt();
        title = in.readString();
        unicode=in.readString();
        position=in.readInt();
        status=in.readInt();
        created_at=in.readString();
        updated_at=in.readString();

    }

    public static final Creator<zGroup> CREATOR = new Creator<zGroup>() {
        @Override
        public zGroup createFromParcel(Parcel in) {
            return new zGroup(in);
        }

        @Override
        public zGroup[] newArray(int size) {
            return new zGroup[size];
        }
    };

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

    public zGroup(int id, String title) {
        this.group_link_id = id;
        this.title = title;
        this.hasNoChild=true;
    }

    public zGroup(int id, int url_id, char orientation, String title, String icon, int position, int status,String created_at, String updated_at) {
        this.group_link_id = id;
        this.account_id = url_id;
        this.orientation = orientation;
        this.title= title;
        this.unicode = icon;
        this.position = position;
        this.status = status;
        this.created_at = created_at;
        this.updated_at=updated_at;
        hasNoChild=true;
    }

    public zGroup(int id, ArrayList<zSource> zSource, String title) {
        this.group_link_id = id;
        this.zSource = zSource;
        this.title= title;
        hasNoChild=false;
    }

    public zGroup(char orientation, String title, final String icon, int status) {
        this.orientation = orientation;
        this.title = title;
        this.unicode= icon;
        this.status = status;
        hasNoChild=true;
    }

    public zGroup(char orientation, String title, final String unicode, int status,int group_link_id) {
        this.group_link_id = group_link_id;
        this.orientation = orientation;
        this.status=status;
        this.title = title;
        this.unicode = unicode;
    }



    public int getGroup_link_id() {
        return group_link_id;
    }

    public void setGroup_link_id(int group_link_id) {
        this.group_link_id = group_link_id;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public char getOrientation() {
        return orientation;
    }

    public String getIcon() {
        return unicode;
    }

    public int getStatus() {
        return status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isHasNoChild() {
        return hasNoChild;
    }

    public void setHasNoChild(boolean hasNoChild) {
        this.hasNoChild = hasNoChild;
    }

    public ArrayList<zSource> getzSource() {
        return zSource;
    }

    public void setzSource(ArrayList<zSource> zSource) {
        this.zSource = zSource;
    }

    public String getTitle() {
        return title;
    }

    public void setOrientation(char orientation) {
        this.orientation = orientation;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUnicode(String unicode) {
        this.unicode = unicode;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(group_link_id);
        dest.writeInt(account_id);
        dest.writeString(title);
        dest.writeString(unicode);
        dest.writeInt(position);
        dest.writeInt(status);
        dest.writeString(created_at);
        dest.writeString(updated_at);

    }
}
