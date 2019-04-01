package com.example.user.zeeals.model;

import android.os.Parcel;
import android.os.Parcelable;

public class zSource extends Zlink implements Parcelable {

    protected zSource(Parcel in) {
        id = in.readInt();
        sourceName = in.readString();
        sourceLink = in.readString();
        parentID = in.readInt();
    }

    public static final Creator<zSource> CREATOR = new Creator<zSource>() {
        @Override
        public zSource createFromParcel(Parcel in) {
            return new zSource(in);
        }

        @Override
        public zSource[] newArray(int size) {
            return new zSource[size];
        }
    };

    @Override
    public boolean isParent() {
        return false;
    }

    private int id;
    private String sourceName;
    private String sourceLink;
    private int parentID;

    public zSource(String sourceName, String sourceLink) {
        this.sourceName = sourceName;
        this.sourceLink = sourceLink;
    }

    public zSource(int id, String sourceName, String sourceLink, int parentID) {
        this.id = id;
        this.sourceName = sourceName;
        this.sourceLink = sourceLink;
        this.parentID = parentID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(sourceName);
        dest.writeString(sourceLink);
        dest.writeInt(parentID);
    }
}
