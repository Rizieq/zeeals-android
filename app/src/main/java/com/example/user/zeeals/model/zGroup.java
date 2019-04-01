package com.example.user.zeeals.model;

import android.os.Parcel;
import android.os.Parcelable;

public class zGroup extends Zlink implements Parcelable {

    protected zGroup(Parcel in) {
        id = in.readInt();
        name = in.readString();
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


    private int id;
    private zSource[] zSource;
    private boolean childrenVisible;

    public boolean isChildrenVisible() {
        return childrenVisible;
    }

    public void setChildrenVisible(boolean childrenVisible) {
        this.childrenVisible = childrenVisible;
    }


    public zGroup(int id, com.example.user.zeeals.model.zSource[] zSource, String name) {
        this.id = id;
        this.zSource = zSource;
        this.name = name;
    }

    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public zSource[] getzSource() {
        return zSource;
    }

    public void setzSource(zSource[] zSource) {
        this.zSource = zSource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }
}
