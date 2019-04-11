package com.example.user.zeeals.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class zGroup extends Zlink implements Parcelable {

    protected zGroup(Parcel in) {
        id = in.readInt();
        title = in.readString();
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


    @SerializedName("id")
    private int id;
    @SerializedName("zSource")
    private ArrayList<zSource> zSource;
    private boolean hasNoChild;
    private boolean childrenVisible;

    private int url_id;
    private String orientation;
    private String title;
    private String icon;
    private int position;
    private String status;
    private String created_at;
    private String updated_at;


    public boolean isChildrenVisible() {
        return childrenVisible;
    }

    public void setChildrenVisible(boolean childrenVisible) {
        this.childrenVisible = childrenVisible;
    }

    public zGroup(int id, String title) {
        this.id = id;
        this.title = title;
        this.hasNoChild=true;
    }

    public zGroup(int id, int url_id, String orientation, String title, String icon, int position, String status,String created_at, String updated_at) {
        this.id = id;
        this.url_id = url_id;
        this.orientation = orientation;
        this.title= title;
        this.icon = icon;
        this.position = position;
        this.status = status;
        this.created_at = created_at;
        this.updated_at=updated_at;
        hasNoChild=true;
    }

    public zGroup(int id, ArrayList<zSource> zSource, String title) {
        this.id = id;
        this.zSource = zSource;
        this.title= title;
        hasNoChild=false;
    }


    public int getUrl_id() {
        return url_id;
    }

    public String getOrientation() {
        return orientation;
    }

    public String getIcon() {
        return icon;
    }

    public String getStatus() {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setRame(String title) {
        this.title= title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
    }
}
